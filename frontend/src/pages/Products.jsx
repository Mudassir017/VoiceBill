import { useEffect, useState } from "react";
import api from "../services/api";
import { Card } from "../components/ui/Card";
import { Button } from "../components/ui/Button";

export default function Products() {
  const [items, setItems] = useState([]);
  const [name, setName] = useState("");
  const load = async () => setItems((await api.get("/products")).data);
  useEffect(() => { load(); }, []);
  const add = async (e) => {
    e.preventDefault();
    await api.post("/products", { name, unit: "pcs", price: 0 });
    setName("");
    load();
  };
  return (
    <Card>
      <h1 className="font-eb-garamond text-3xl">Products</h1>
      <form onSubmit={add} className="mt-4 flex gap-2">
        <input className="border-2 border-vast-ink rounded-xl px-3 py-2" value={name} onChange={(e) => setName(e.target.value)} placeholder="Product name" />
        <Button type="submit">Add</Button>
      </form>
      <ul className="mt-4 space-y-2">
        {items.map((p) => <li key={p.id}>{p.name} — {p.price} {p.unit}</li>)}
      </ul>
    </Card>
  );
}
