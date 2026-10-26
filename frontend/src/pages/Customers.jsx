import { useEffect, useState } from "react";
import api from "../services/api";
import { Card } from "../components/ui/Card";
import { Button } from "../components/ui/Button";

export default function Customers() {
  const [items, setItems] = useState([]);
  const [name, setName] = useState("");
  const load = async () => setItems((await api.get("/customers")).data);
  useEffect(() => { load(); }, []);
  const add = async (e) => {
    e.preventDefault();
    await api.post("/customers", { name });
    setName("");
    load();
  };
  return (
    <Card>
      <h1 className="font-eb-garamond text-3xl">Customers</h1>
      <form onSubmit={add} className="mt-4 flex gap-2">
        <input className="border-2 border-vast-ink rounded-xl px-3 py-2" value={name} onChange={(e) => setName(e.target.value)} placeholder="Customer name" />
        <Button type="submit">Add</Button>
      </form>
      <ul className="mt-4 space-y-2">
        {items.map((c) => <li key={c.id}>{c.name}</li>)}
      </ul>
    </Card>
  );
}
