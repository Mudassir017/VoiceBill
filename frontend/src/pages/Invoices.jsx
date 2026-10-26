import { useEffect, useState } from "react";
import api from "../services/api";
import { Card } from "../components/ui/Card";

export default function Invoices() {
  const [items, setItems] = useState([]);
  useEffect(() => { (async () => setItems((await api.get("/invoices")).data))(); }, []);
  return (
    <Card>
      <h1 className="font-eb-garamond text-3xl">Invoices</h1>
      <ul className="mt-4 space-y-2">
        {items.map((i) => (
          <li key={i.id} className="flex items-center justify-between">
            <span>{i.invoiceNumber} — {i.customerName} — {i.total}</span>
            <a className="underline" href={`${import.meta.env.VITE_API_BASE_URL || "http://localhost:8080"}/api/invoices/${i.id}/pdf`} target="_blank" rel="noreferrer">Download PDF</a>
          </li>
        ))}
      </ul>
    </Card>
  );
}
