import { useEffect, useState } from "react";
import api from "../services/api";
import { Card } from "../components/ui/Card";
import { Button } from "../components/ui/Button";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts";

export default function Dashboard() {
  const [summary, setSummary] = useState(null);
  const [recent, setRecent] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const [s, r] = await Promise.all([
          api.get("/dashboard/summary"),
          api.get("/dashboard/recent"),
        ]);
        setSummary(s.data);
        setRecent(r.data);
      } catch {
        setError("Failed to load dashboard");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <Card>Loading...</Card>;
  if (error) return <Card>{error}</Card>;

  const chartData = recent.map((i) => ({ name: i.invoiceNumber, amount: Number(i.total) }));

  return (
    <div className="space-y-6">
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <p className="text-sm text-fog">Total invoices</p>
          <p className="font-eb-garamond text-4xl">{summary.totalInvoices}</p>
        </Card>
        <Card>
          <p className="text-sm text-fog">Total invoiced</p>
          <p className="font-eb-garamond text-4xl">{summary.totalAmount}</p>
        </Card>
      </div>
      <Card>
        <h2 className="font-eb-garamond text-2xl">Recent invoices</h2>
        {recent.length === 0 ? (
          <p className="mt-2 text-fog">No invoices yet.</p>
        ) : (
          <ul className="mt-3 space-y-2">
            {recent.map((i) => (
              <li key={i.id}>{i.invoiceNumber} — {i.customerName} — {i.total}</li>
            ))}
          </ul>
        )}
      </Card>
      <Card>
        <h2 className="font-eb-garamond text-2xl">Revenue</h2>
        <div className="mt-4 h-64">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData}>
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="amount" fill="#f0d7ff" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </Card>
      <Button onClick={() => (window.location.href = "/invoices/new")}>New Invoice</Button>
    </div>
  );
}
