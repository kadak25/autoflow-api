import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../lib/api";

type Workflow = {
  id: number;
  name: string;
  description: string;
};

export default function Dashboard() {
  const [items, setItems] = useState<Workflow[]>([]);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);

  const nav = useNavigate();

  async function load() {
    setLoading(true);
    try {
      const res = await api.get<Workflow[]>("/workflows/me");
      setItems(res.data);
    } finally {
      setLoading(false);
    }
  }

  async function create() {
    if (!name.trim()) return;

    const res = await api.post<Workflow>("/workflows", {
      name,
      description,
    });

    setName("");
    setDescription("");
    await load();
    nav(`/workflows/${res.data.id}`);
  }

  // ✅ DELETE WORKFLOW
  async function deleteWorkflow(id: number) {
    const ok = confirm("Delete this workflow?");
    if (!ok) return;

    await api.delete(`/workflows/${id}`);
    load();
  }

  function logout() {
    localStorage.removeItem("token");
    nav("/login");
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <div className="page">
      <div className="topbar">
        <div>
          <div className="title">AutoFlow</div>
          <div className="muted">Workflows dashboard</div>
        </div>

        <div className="row">
          <button className="btn" onClick={load} disabled={loading}>
            {loading ? "Refreshing..." : "Refresh"}
          </button>

          <button className="btn danger" onClick={logout}>
            Logout
          </button>
        </div>
      </div>

      <div className="grid2">
        {/* CREATE */}
        <div className="card">
          <h2 className="h2">New Workflow</h2>

          <div className="field">
            <label>Name</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. HTTP → AI Summary"
            />
          </div>

          <div className="field">
            <label>Description</label>
            <input
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="short description"
            />
          </div>

          <button className="btn primary" onClick={create}>
            Create
          </button>
        </div>

        {/* LIST */}
        <div className="card">
          <h2 className="h2">My Workflows</h2>

          <div className="cards">
            {items.map((w) => (
              <div key={w.id} className="wfcardRow">
                {/* CARD */}
                <button
                  className="wfcard"
                  onClick={() => nav(`/workflows/${w.id}`)}
                >
                  <div className="wfname">{w.name}</div>
                  <div className="muted">{w.description}</div>
                  <div className="pill">#{w.id}</div>
                </button>

                {/* DELETE */}
                <button
                  className="deleteBtn"
                  onClick={(e) => {
                    e.stopPropagation();
                    deleteWorkflow(w.id);
                  }}
                >
                  🗑
                </button>
              </div>
            ))}

            {items.length === 0 && (
              <div className="muted">No workflows yet.</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
