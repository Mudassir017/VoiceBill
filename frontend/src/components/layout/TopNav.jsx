import { Button } from "../ui/Button";

export function TopNav({ onToggleTheme }) {
  return (
    <header className="sticky top-0 z-10 flex items-center justify-between rounded-full border-2 border-vast-ink bg-lumen-cream px-6 py-3 mx-4 mt-4">
      <span className="font-eb-garamond text-xl">VoiceBill</span>
      <div className="flex items-center gap-2">
        <Button variant="ghost" onClick={onToggleTheme}>Theme</Button>
        <Button variant="primary">New Invoice</Button>
      </div>
    </header>
  );
}
