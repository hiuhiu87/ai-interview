import { useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { adminCatalogService } from "@/services/adminCatalogService";
import type { QuestionItem, TemplateItem } from "@/types";
import type { GeneratorFormState } from "../question-builder.utils";

type QuestionSetGeneratorProps = {
  templates: TemplateItem[];
  form: GeneratorFormState;
  onFormChange: (next: GeneratorFormState) => void;
};

export function QuestionSetGenerator({ templates, form, onFormChange }: QuestionSetGeneratorProps) {
  const [generatedQuestions, setGeneratedQuestions] = useState<QuestionItem[]>([]);

  const handleGenerate = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!form.templateId) {
      toast.error("Select a template");
      return;
    }

    try {
      const response = await adminCatalogService.generateQuestionSet({
        templateId: Number(form.templateId),
        difficultyRuleConfig: {
          easy: Number(form.easy || 0),
          medium: Number(form.medium || 0),
          hard: Number(form.hard || 0),
        },
      });
      setGeneratedQuestions(response.questions);
      toast.success("Question set generated");
    } catch {
      toast.error("Could not generate question set");
    }
  };

  return (
    <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
      <div className="flex flex-col gap-2 lg:flex-row lg:items-end lg:justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-[0.24em] text-muted-foreground">Question Set Generator</p>
          <h2 className="mt-2 text-2xl font-semibold tracking-tight">Assemble a valid interview set from template rules</h2>
          <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
            Choose a template and define the difficulty distribution. The CMS service will return a randomized, deduplicated question set.
          </p>
        </div>
        <div className="rounded-2xl bg-emerald-50 px-4 py-3 text-sm text-emerald-900 ring-1 ring-emerald-200">
          <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-emerald-700">Generated</div>
          <div className="mt-1 text-2xl font-semibold">{generatedQuestions.length}</div>
        </div>
      </div>

      <form onSubmit={handleGenerate} className="mt-6 grid gap-4 xl:grid-cols-[1.4fr_0.6fr_0.6fr_0.6fr_auto]">
        <label className="space-y-2">
          <span className="text-sm font-medium">Template</span>
          <Select value={form.templateId || "none"} onValueChange={(value) => onFormChange({ ...form, templateId: value === "none" ? "" : value })}>
            <SelectTrigger className="h-10 w-full">
              <SelectValue placeholder="Select template" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="none">Select template</SelectItem>
              {templates.map((template) => (
                <SelectItem key={template.id} value={String(template.id)}>{template.name}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </label>

        <label className="space-y-2">
          <span className="text-sm font-medium">Easy</span>
          <Input type="number" min={0} value={form.easy} onChange={(event) => onFormChange({ ...form, easy: event.target.value })} />
        </label>

        <label className="space-y-2">
          <span className="text-sm font-medium">Medium</span>
          <Input type="number" min={0} value={form.medium} onChange={(event) => onFormChange({ ...form, medium: event.target.value })} />
        </label>

        <label className="space-y-2">
          <span className="text-sm font-medium">Hard</span>
          <Input type="number" min={0} value={form.hard} onChange={(event) => onFormChange({ ...form, hard: event.target.value })} />
        </label>

        <div className="flex items-end">
          <Button type="submit">Generate set</Button>
        </div>
      </form>

      <div className="mt-6 grid gap-4">
        {generatedQuestions.length === 0 ? (
          <div className="rounded-2xl border border-dashed border-border px-4 py-10 text-center text-sm text-muted-foreground">
            Generate a question set to inspect the final interview payload.
          </div>
        ) : (
          generatedQuestions.map((question) => (
            <article key={question.id} className="rounded-2xl border border-border bg-background p-4">
              <div className="flex flex-wrap items-center gap-2 text-xs uppercase tracking-[0.16em] text-muted-foreground">
                <span>{question.difficulty}</span>
                <span>{question.level}</span>
                {question.skillTags.map((tag) => (
                  <span key={tag.id} className="rounded-full bg-muted px-2 py-1 text-[11px] font-medium tracking-normal text-foreground">
                    {tag.name}
                  </span>
                ))}
              </div>
              <h3 className="mt-3 text-lg font-medium">{question.content}</h3>
              <p className="mt-2 text-sm text-muted-foreground">{question.expectedAnswer || "No expected answer"}</p>
              <div className="mt-3 flex flex-wrap gap-2">
                {question.keywords.map((keyword) => (
                  <span key={keyword} className="rounded-full border border-border px-3 py-1 text-xs">
                    {keyword}
                  </span>
                ))}
              </div>
              <div className="mt-4 grid gap-3 md:grid-cols-2">
                {question.rubrics.map((rubric, index) => (
                  <div key={`${question.id}-rubric-${index}`} className="rounded-2xl border border-border px-3 py-3">
                    <div className="text-xs font-semibold uppercase tracking-[0.18em] text-muted-foreground">Score {rubric.scoreLevel}</div>
                    <div className="mt-2 text-sm">{rubric.criteriaDescription}</div>
                  </div>
                ))}
              </div>
            </article>
          ))
        )}
      </div>
    </div>
  );
}
