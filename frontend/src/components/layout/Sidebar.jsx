import { Button } from "../ui/Button";

const items = ["Dashboard", "Invoices", "Products", "Customers", "Settings"];

export function Sidebar() {
  return (
    <aside className="hidden md:flex w-60 flex-col gap-2 border-r-2 border-vast-ink bg-lumen-cream p-4">
      <div className="font-eb-garamond text-2xl mb-4">VoiceBill</div>
      {items.map((it) => (
        <button key={it} className="text-left px-3 py-2 rounded-xl hover:bg-lumen-stone">
          {it}
        </button>
      ))}
    </aside>
  );
}
