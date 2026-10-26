import { useState } from "react";
import { Sidebar } from "./Sidebar";
import { TopNav } from "./TopNav";

export function AppLayout({ children }) {
  const [dark, setDark] = useState(false);
  return (
    <div className={dark ? "dark" : ""}>
      <div className="min-h-screen bg-lumen-cream text-vast-ink flex">
        <Sidebar />
        <div className="flex-1 flex flex-col">
          <TopNav onToggleTheme={() => setDark((d) => !d)} />
          <main className="p-6">{children}</main>
        </div>
      </div>
    </div>
  );
}
