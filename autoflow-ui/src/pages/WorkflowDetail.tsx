import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../lib/api";

type Workflow = { id: number; name: string; description: string };

type StepType = "HTTP" | "AI_SUMMARY";

type Step = {
  id: number;
  workflowId: number;
  name: string;
  type: StepType;
  orderIndex: number;
  config: string; // JSON string
};

type StepRunResult = {
  stepId: number;
  stepName: string;
  type: StepType;
  status: "SUCCESS" | "FAILED";
  httpStatus?: number | null;
  error?: string | null;
  output?: string | null;
};

type ExecutionResult = {
  workflowId: number;
  status: "SUCCESS" | "FAILED";
  steps: StepRunResult[];
};

export default function WorkflowDetail() {
  const { id } = useParams();
  const workflowId = Number(id);
  const nav = useNavigate();

  const [wf, setWf] = useState<Workflow | null>(null);
  const [steps, setSteps] = useState<Step[]>([]);
  const [lastRun, setLastRun] = useState<ExecutionResult | null>(null);
  const [loading, setLoading] = useState(false);

  // form
  const [name, setName] = useState("");
  const [type, setType] = useState<StepType>("HTTP");
  const [orderIndex, setOrderIndex] = useState(1);

  const [httpMethod, setHttpMethod] = useState("GET");
  const [httpUrl, setHttpUrl] = useState("https://example.com");

  const [model, setModel] = useState("facebook/bart-large-cnn");
  const [input, setInput] = useState("Summarize this: {{step.1.output}}");
  const [maxChars, setMaxChars] = useState(800);

  const configPreview = useMemo(() => {
    if (type === "HTTP") {
      return JSON.stringify({ url: httpUrl, method: httpMethod }, null, 2);
    }
    return JSON.stringify({ model, input, maxChars }, null, 2);
  }, [type, httpUrl, httpMethod, model, input, maxChars]);

  async function load() {
    setLoading(true);
    try {
      const wfRes = await api.get<Workflow>(`/workflows/${workflowId}`);
      setWf(wfRes.data);

      const stepsRes = await api.get<Step[]>(`/steps/workflow/${workflowId}`);
      setSteps(stepsRes.data);
    } finally {
      setLoading(false);
    }
  }

  async function createStep() {
    if (!name.trim()) return;

    let configObj: any;
    if (type === "HTTP") configObj = { url: httpUrl, method: httpMethod };
    else configObj = { model, input, maxChars };

    const payload = {
      workflowId,
      name,
      type,
      orderIndex,
      config: JSON.stringify(configObj),
    };

    await api.post(`/steps`, payload);

    setName("");
    await load();
  }

  async function del(stepId: number) {
    await api.delete(`/steps/${stepId}`);
    await load();
  }

  async function run() {
    setLoading(true);
    try {
      const res = await api.post<ExecutionResult>(`/workflows/${workflowId}/run`);
      setLastRun(res.data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (!Number.isFinite(workflowId)) return;
    load();
  }, [workflowId]);

  return (
    <div className="page">
      <div className="topbar">
        <div className="row">
          <button className="btn" onClick={() => nav("/dashboard")}>← Back</button>
          <button className="btn" onClick={load} disabled={loading}>{loading ? "Loading..." : "Refresh"}</button>
          <button className="btn primary" onClick={run} disabled={loading}>Run</button>
        </div>
      </div>

      <div className="card">
        <div className="title">{wf?.name ?? `Workflow #${workflowId}`}</div>
        <div className="muted">{wf?.description}</div>
      </div>

      <div className="grid2">
        <div className="card">
          <h2 className="h2">Steps</h2>
          <div className="cards">
            {steps.map((s) => (
              <div key={s.id} className="stepcard">
                <div className="row space">
                  <div>
                    <div className="wfname">
                      #{s.orderIndex} • {s.name}
                    </div>
                    <div className="muted">{s.type} • id {s.id}</div>
                  </div>
                  <button className="btn danger" onClick={() => del(s.id)}>Delete</button>
                </div>

                <details>
                  <summary className="muted">Config</summary>
                  <pre className="code">{prettyJsonString(s.config)}</pre>
                </details>
              </div>
            ))}
            {steps.length === 0 && <div className="muted">No steps yet.</div>}
          </div>
        </div>

        <div className="card">
          <h2 className="h2">Add Step</h2>

          <div className="field">
            <label>Name</label>
            <input value={name} onChange={(e) => setName(e.target.value)} placeholder="e.g. Fetch homepage" />
          </div>

          <div className="grid2">
            <div className="field">
              <label>Type</label>
              <select value={type} onChange={(e) => setType(e.target.value as StepType)}>
                <option value="HTTP">HTTP</option>
                <option value="AI_SUMMARY">AI_SUMMARY</option>
              </select>
            </div>

            <div className="field">
              <label>Order</label>
              <input
                type="number"
                value={orderIndex}
                onChange={(e) => setOrderIndex(Number(e.target.value))}
              />
            </div>
          </div>

          {type === "HTTP" ? (
            <div className="grid2">
              <div className="field">
                <label>Method</label>
                <select value={httpMethod} onChange={(e) => setHttpMethod(e.target.value)}>
                  <option>GET</option>
                  <option>POST</option>
                  <option>PUT</option>
                  <option>DELETE</option>
                </select>
              </div>
              <div className="field">
                <label>URL</label>
                <input value={httpUrl} onChange={(e) => setHttpUrl(e.target.value)} />
              </div>
            </div>
          ) : (
            <>
              <>
                <div className="field">
                  <label>Model</label>
                  <input value={model} onChange={(e) => setModel(e.target.value)} />
                </div>

                <div className="field">
                  <label>Input (supports {"{{step.N.output}}"})</label>
                  <textarea
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    rows={3}
                  />
                </div>

                <div className="field">
                  <label>Max chars</label>
                  <input
                    type="number"
                    value={maxChars}
                    onChange={(e) => setMaxChars(Number(e.target.value))}
                  />
                </div>
              </>

            </>
          )}

          <details open>
            <summary className="muted">Config preview</summary>
            <pre className="code">{configPreview}</pre>
          </details>

          <button className="btn primary" onClick={createStep}>Create Step</button>

          <hr className="hr" />

          <h2 className="h2">Last Run</h2>
          {!lastRun ? (
            <div className="muted">No run yet. Click Run.</div>
          ) : (
            <div>
              <div className={`pill ${lastRun.status === "SUCCESS" ? "ok" : "bad"}`}>
                {lastRun.status}
              </div>

              <div className="cards">
                {lastRun.steps.map((r) => (
                  <div key={r.stepId} className="runCard">
                    <div className="row space">
                      <div>
                        <div className="wfname">{r.stepName}</div>
                        <div className="muted">{r.type} • {r.status}{r.httpStatus ? ` • HTTP ${r.httpStatus}` : ""}</div>
                      </div>
                      {r.error && <div className="pill bad">error</div>}
                    </div>

                    {r.error && <div className="alert">{r.error}</div>}

                    {r.output && (
                      <details>
                        <summary className="muted">Output (preview)</summary>
                        <pre className="code">
                          {previewText(r.output)}
                        </pre>
                      </details>
                    )}
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function prettyJsonString(s: string) {
  try {
    return JSON.stringify(JSON.parse(s), null, 2);
  } catch {
    return s;
  }
}

function previewText(s: string) {
  const max = 1200;
  if (s.length <= max) return s;
  return s.slice(0, max) + "\n\n... (truncated)";
}
