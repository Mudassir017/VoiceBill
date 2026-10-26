export function Card({ children, dark = false, className = "" }) {
  return (
    <div
      className={`rounded-[32px] p-8 ${
        dark ? "bg-vast-ink text-lumen-cream" : "bg-lumen-cream text-vast-ink border-2 border-vast-ink"
      } ${className}`}
    >
      {children}
    </div>
  );
}
