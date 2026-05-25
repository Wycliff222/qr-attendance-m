const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const { Pool } = require("pg");
const qr = require("qrcode");

const app = express();
app.use(cors());
app.use(bodyParser.json());

// PostgreSQL connection
const pool = new Pool({
  connectionString: process.env.DATABASE_URL, // Render will provide this
  ssl: { rejectUnauthorized: false }
});

// Admin generates trainee registration codes
app.post("/generate-code", async (req, res) => {
  const { adminId } = req.body;
  const code = Math.random().toString(36).substring(2, 8).toUpperCase();
  await pool.query("INSERT INTO trainee_codes(admin_id, code) VALUES($1,$2)", [adminId, code]);
  res.json({ code });
});

// Trainee creates account using code
app.post("/register-trainee", async (req, res) => {
  const { name, email, code } = req.body;
  const validCode = await pool.query("SELECT * FROM trainee_codes WHERE code=$1", [code]);
  if (validCode.rows.length === 0) return res.status(400).json({ error: "Invalid code" });

  await pool.query("INSERT INTO trainees(name,email) VALUES($1,$2)", [name, email]);
  res.json({ message: "Trainee registered successfully" });
});

// Create session and generate QR
app.post("/create-session", async (req, res) => {
  const { sessionName } = req.body;
  const session = await pool.query("INSERT INTO sessions(name) VALUES($1) RETURNING id", [sessionName]);
  const qrCode = await qr.toDataURL(session:${session.rows[0].id});
  res.json({ sessionId: session.rows[0].id, qrCode });
});

// Student scans QR to mark attendance
app.post("/mark-attendance", async (req, res) => {
  const { studentId, sessionId } = req.body;
  await pool.query("INSERT INTO attendance(student_id, session_id) VALUES($1,$2)", [studentId, sessionId]);
  res.json({ message: "Attendance marked" });
});

app.listen(5000, () => console.log("Backend running on port 5000"));