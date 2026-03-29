import { Input } from "./components/ui/input";
import { DemoDialog } from "./components/demo/DemoDialog";
import DemoTable from "./components/demo/DemoTable";
import { Sidebar } from "./components/layout/Sidebar";
import { BrowserRouter } from "react-router-dom";
import AppRouter from "./router";
import { Toaster } from "sonner";

function App() {

  return (
    <main className="app-shell">
      <BrowserRouter>
      <AppRouter/>
      <Toaster position="top-right" />
      </BrowserRouter>
    </main>
  )
}

export default App
