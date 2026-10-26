export function Badge({ children, variant = "teal", className = "" }) {
  const styles = {
    teal: "bg-forest-ink text-lumen-cream",
    dark: "bg-vast-ink text-lumen-cream rounded-lg",
    outline: "bg-transparent text-lumen-cream border border-pure-white rounded-full",
  };
  return (
    <span className={`inline-flex items-center rounded-full px-4 py-2 text-xs font-medium ${styles[variant]} ${className}`}>
      {children}
    </span>
  );
}
