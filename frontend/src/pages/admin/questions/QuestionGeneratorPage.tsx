import { useEffect, useState } from "react";
import { toast } from "sonner";
import { adminCatalogService } from "@/services/adminCatalogService";
import type { TemplateItem } from "@/types/admin-catalog.types";
import { QuestionSetGenerator } from "./components/QuestionSetGenerator";
import { 
  defaultGeneratorForm, 
  type GeneratorFormState 
} from "./question-builder.utils";

export default function QuestionGeneratorPage() {
  const [templates, setTemplates] = useState<TemplateItem[]>([]);
  const [generatorForm, setGeneratorForm] = useState<GeneratorFormState>(defaultGeneratorForm);

  const loadMetadata = async () => {
    try {
      const metadata = await adminCatalogService.getMetadata();
      setTemplates(metadata.templates);
    } catch {
      toast.error("Failed to load templates for generator");
    }
  };

  useEffect(() => {
    void loadMetadata();
  }, []);

  return (
    <div className="space-y-6">
      <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
        <p className="text-xs font-semibold uppercase tracking-[0.24em] text-muted-foreground">AI Tools</p>
        <h1 className="mt-3 text-3xl font-semibold tracking-tight">Question Set Generator</h1>
        <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
          Assemble randomized, high-quality interview sets based on template rules and difficulty distribution.
        </p>
      </div>

      <QuestionSetGenerator
        templates={templates}
        form={generatorForm}
        onFormChange={setGeneratorForm}
      />
    </div>
  );
}
