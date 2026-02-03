import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../lib/api";

export default function Login() {
  const [email, setEmail] = useState("mustafa@test.com");
  const [password, setPassword] = useState("123456");
  const [err, setErr] = useState<string | null>(null);
  const nav = useNavigate();

  async function handleLogin() {
    setErr(null);
    try {
      const res = await api.post("/auth/login", { email, password });
      localStorage.setItem("token", res.data.token);
      nav("/dashboard");
    } catch (e: any) {
      setErr(e?.response?.data?.message ?? "Login failed");
    }
  }

  return (
    <div className="page center">
      <div className="card w-420">
        <h1 className="h1">AutoFlow</h1>
        <p className="muted">Login to your workspace</p>

        <div className="field">
          <label>Email</label>
          <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="email" />
        </div>

        <div className="field">
          <label>Password</label>
          <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="password" />
        </div>

        {err && <div className="alert">{err}</div>}

        <button className="btn primary" onClick={handleLogin}>
          Login
        </button>
      </div>
    </div>
  );
}
