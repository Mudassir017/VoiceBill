import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { Button } from "../components/ui/Button";
import { Card } from "../components/ui/Card";

export default function Register() {
  const { register } = useAuth();
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [err, setErr] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    try { await register(fullName, email, password); window.location.href = "/"; }
    catch { setErr("Registration failed"); }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-lumen-cream">
      <Card className="w-full max-w-md">
        <h1 className="font-eb-garamond text-3xl">Register</h1>
        <form onSubmit={submit} className="mt-4 flex flex-col gap-3">
          <input className="border-2 border-vast-ink rounded-xl px-3 py-2" placeholder="Full name" value={fullName} onChange={(e) => setFullName(e.target.value)} />
          <input className="border-2 border-vast-ink rounded-xl px-3 py-2" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
          <input className="border-2 border-vast-ink rounded-xl px-3 py-2" type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
          {err && <p className="text-red-600 text-sm">{err}</p>}
          <Button type="submit">Create account</Button>
        </form>
      </Card>
    </div>
  );
}
