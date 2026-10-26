import { useEffect, useState } from "react";
import api from "../services/api";
import { Card } from "../components/ui/Card";
import VoiceInput from "../components/voice/VoiceInput";

export default function CreateInvoice() {
  const [products, setProducts] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [customerId, setCustomerId] = useState("");
  const [items, setItems] = useState([]);
  const [productId, setProductId] = useState("");
  const [qty, setQty] = useState(1);
  const handleTranscript = async (t) => {
    try {
      const { data } = await api.post("/voice/interpret", { transcript: t });
      if (data.customerId) setCustomerId(String(data.customerId));
      if (data.items) {
        setItems(data.items.map((it) => ({ productId: it.productId, quantity: Number(it.quantity) })));
      }
    } catch (e) {
      alert("Interpretation failed");
    }
  };

  useEffect(() => {
    (async () => {
      setProducts((await api.get("/products")).data);
      setCustomers((await api.get("/customers")).data);
    })();
  }, []);

  const addItem = (e) => {
    e.preventDefault();
    if (!productId) return;
    setItems([...items, { productId: Number(productId), quantity: Number(qty) }]);
    setProductId("");
    setQty(1);
  };

  const submit = async (e) => {
    e.preventDefault();
    const { data } = await api.post("/invoices", {
      customerId: customerId ? Number(customerId) : null,
      invoiceDate: new Date().toISOString().slice(0, 10),
      items,
    });
    setResult(data);
  };

  return (
    <Card>
      <h1 className="font-eb-garamond text-3xl">Create Invoice</h1>
      <VoiceInput onTranscript={handleTranscript} />
      <form onSubmit={submit} className="mt-4 flex flex-col gap-3">
        <select className="border-2 border-vast-ink rounded-xl px-3 py-2" value={customerId} onChange={(e) => setCustomerId(e.target.value)}>
          <option value="">Select customer</option>
          {customers.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
        </select>
        <div className="flex gap-2">
          <select className="border-2 border-vast-ink rounded-xl px-3 py-2" value={productId} onChange={(e) => setProductId(e.target.value)}>
            <option value="">Select product</option>
            {products.map((p) => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
          <input type="number" className="border-2 border-vast-ink rounded-xl px-3 py-2 w-24" value={qty} onChange={(e) => setQty(e.target.value)} />
          <Button type="button" onClick={addItem}>Add item</Button>
        </div>
        <ul className="space-y-1">
          {items.map((it, i) => <li key={i}>Product {it.productId} x {it.quantity}</li>)}
        </ul>
        <Button type="submit">Create Invoice</Button>
      </form>
      {result && <p className="mt-4">Created {result.invoiceNumber} — Total {result.total}</p>}
    </Card>
  );
}
