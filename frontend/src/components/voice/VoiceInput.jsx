import { useEffect, useRef, useState } from "react";
import { Button } from "../ui/Button";
import { Card } from "../ui/Card";

export default function VoiceInput({ onTranscript }) {
  const [supported, setSupported] = useState(false);
  const [listening, setListening] = useState(false);
  const [transcript, setTranscript] = useState("");
  const [error, setError] = useState("");
  const recRef = useRef(null);

  useEffect(() => {
    const SR = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (SR) setSupported(true);
    return () => { if (recRef.current) recRef.current.stop(); };
  }, []);

  const start = () => {
    const SR = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SR) { setError("Speech recognition not supported"); return; }
    setError("");
    const rec = new SR();
    rec.lang = "en-US";
    rec.interimResults = true;
    rec.onresult = (e) => {
      let t = "";
      for (let i = 0; i < e.results.length; i++) t += e.results[i][0].transcript;
      setTranscript(t);
    };
    rec.onerror = (e) => setError(e.error || "Recognition error");
    rec.onend = () => setListening(false);
    recRef.current = rec;
    rec.start();
    setListening(true);
  };

  const stop = () => { if (recRef.current) recRef.current.stop(); setListening(false); };

  if (!supported) {
    return <Card><p className="text-fog">Speech recognition not supported in this browser. Use manual entry.</p></Card>;
  }

  return (
    <Card>
      <div className="flex items-center gap-2">
        <Button type="button" onClick={listening ? stop : start} variant={listening ? "secondary" : "primary"}>
          {listening ? "Stop" : "Start mic"}
        </Button>
        {listening && <span className="text-ember-glow font-medium">Listening...</span>}
      </div>
      {error && <p className="mt-2 text-red-600 text-sm">{error}</p>}
      <textarea
        className="mt-3 w-full border-2 border-vast-ink rounded-xl px-3 py-2"
        rows={3}
        value={transcript}
        onChange={(e) => setTranscript(e.target.value)}
        placeholder="Transcript will appear here..."
      />
      <div className="mt-2">
        <Button type="button" onClick={() => onTranscript && onTranscript(transcript)} disabled={!transcript}>
          Use transcript
        </Button>
      </div>
    </Card>
  );
}
