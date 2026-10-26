export function Button({ children, variant = "primary", className = "", ...props }) {
  const base = "inline-flex items-center gap-2 rounded-xl border-2 border-vast-ink px-4 py-3 font-medium text-sm transition";
  const variants = {
    primary: "bg-lavender-whisper text-vast-ink",
    secondary: "bg-lumen-cream text-vast-ink",
    ghost: "bg-transparent border-transparent",
  };
  return (
    <button className={`${base} ${variants[variant]} ${className}`} {...props}>
      {children}
    </button>
  );
}
