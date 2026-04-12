import { useEffect, useMemo, useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { adminCatalogService } from "@/services/adminCatalogService";
import type { QuestionItem, QuestionPayload, SkillNode, TagItem, TemplateItem } from "@/types";

type QuestionFormState = {
  id?: string;
  content: string;
  expectedAnswer: string;
  keywords: string;
  difficulty: string;
  level: string;
  skillId: string;
  templateId: string;
  tagIds: number[];
};

const defaultForm: QuestionFormState = {
  content: "",
  expectedAnswer: "",
  keywords: "",
  difficulty: "",
  level: "",
  skillId: "",
  templateId: "",
  tagIds: [],
};

const flattenSkills = (skills: SkillNode[], depth = 0): Array<SkillNode & { depth: number }> =>
  skills.flatMap((skill) => [
    { ...skill, depth },
    ...flattenSkills(skill.children, depth + 1),
  ]);

export default function Questions() {
  const [questions, setQuestions] = useState<QuestionItem[]>([]);
  const [skills, setSkills] = useState<SkillNode[]>([]);
  const [tags, setTags] = useState<TagItem[]>([]);
  const [templates, setTemplates] = useState<TemplateItem[]>([]);
  const [difficulties, setDifficulties] = useState<string[]>([]);
  const [form, setForm] = useState<QuestionFormState>(defaultForm);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const flatSkills = useMemo(() => flattenSkills(skills), [skills]);

  const load = async () => {
    try {
      const [metadata, questionItems] = await Promise.all([
        adminCatalogService.getMetadata(),
        adminCatalogService.getQuestions(),
      ]);
      setSkills(metadata.skills);
      setTags(metadata.tags);
      setTemplates(metadata.templates);
      setDifficulties(metadata.difficulties);
      setQuestions(questionItems);
    } catch {
      toast.error("Failed to load question metadata");
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const closeModal = () => {
    setIsModalOpen(false);
    setForm(defaultForm);
  };

  const toggleTag = (tagId: number) => {
    setForm((prev) => ({
      ...prev,
      tagIds: prev.tagIds.includes(tagId)
        ? prev.tagIds.filter((id) => id !== tagId)
        : [...prev.tagIds, tagId],
    }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const payload: QuestionPayload = {
      content: form.content,
      expectedAnswer: form.expectedAnswer,
      keywords: form.keywords
        .split(",")
        .map((item) => item.trim())
        .filter(Boolean),
      difficulty: form.difficulty || undefined,
      level: form.level || "mid",
      skillId: form.skillId ? Number(form.skillId) : null,
      templateId: form.templateId ? Number(form.templateId) : null,
      tagIds: form.tagIds,
      rubrics: [{ scoreLevel: 1, criteriaDescription: "Baseline evaluation criteria" }],
    };

    try {
      if (form.id) {
        await adminCatalogService.updateQuestion(form.id, payload);
        toast.success("Question updated");
      } else {
        await adminCatalogService.createQuestion(payload);
        toast.success("Question created");
      }
      closeModal();
      await load();
    } catch {
      toast.error("Could not save question");
    }
  };

  return (
    <section className="space-y-6">
      <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
        <p className="text-xs font-semibold uppercase tracking-[0.24em] text-muted-foreground">Question Metadata</p>
        <div className="mt-3 flex flex-col gap-4 xl:flex-row xl:items-end xl:justify-between">
          <div>
            <h1 className="text-3xl font-semibold tracking-tight">Standardize how raw questions connect to difficulty, skill, tag, and template</h1>
            <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
              This is the final assembly layer for your admin dataset. Each question carries enough metadata to be filtered, grouped, and turned into a question set reliably.
            </p>
          </div>
          <div className="grid gap-3 sm:grid-cols-2">
            <div className="rounded-2xl bg-rose-50 px-4 py-3 text-sm text-rose-900 ring-1 ring-rose-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-rose-700">Questions</div>
              <div className="mt-1 text-2xl font-semibold">{questions.length}</div>
            </div>
            <div className="rounded-2xl bg-indigo-50 px-4 py-3 text-sm text-indigo-900 ring-1 ring-indigo-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-indigo-700">Mapped Inputs</div>
              <div className="mt-1">Difficulty, skill, template, tag</div>
            </div>
          </div>
        </div>
        <div className="mt-6">
          <Button onClick={() => { setForm(defaultForm); setIsModalOpen(true); }}>
            Add question
          </Button>
        </div>
      </div>

      <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
        <div>
          <h2 className="text-xl font-semibold">Question inventory</h2>
          <p className="mt-1 text-sm text-muted-foreground">Review the normalized dataset that powers question set generation.</p>
        </div>

        <div className="mt-5 grid gap-4">
          {questions.map((question) => (
            <article key={question.id} className="rounded-2xl border border-border bg-background p-4">
              <div className="flex flex-col gap-3 lg:flex-row lg:items-start lg:justify-between">
                <div className="min-w-0 flex-1">
                  <h3 className="font-medium">{question.content}</h3>
                  <div className="mt-2 text-xs uppercase tracking-[0.18em] text-muted-foreground">
                    {question.difficulty || "NO_DIFFICULTY"} {question.skillName ? `· ${question.skillName}` : ""} {question.templateName ? `· ${question.templateName}` : ""}
                  </div>
                  <p className="mt-3 text-sm text-muted-foreground">{question.expectedAnswer || "No expected answer"}</p>
                  <div className="mt-3 flex flex-wrap gap-2">
                    {question.tags.map((tag) => (
                      <span key={tag.id} className="rounded-full bg-muted px-3 py-1 text-xs font-medium">{tag.name}</span>
                    ))}
                    {question.keywords.map((keyword) => (
                      <span key={keyword} className="rounded-full border border-border px-3 py-1 text-xs">{keyword}</span>
                    ))}
                  </div>
                </div>
                <div className="flex shrink-0 gap-1.5">
                  <Button
                    size="xs"
                    variant="outline"
                    onClick={() => {
                      setForm({
                        id: question.id,
                        content: question.content,
                        expectedAnswer: question.expectedAnswer ?? "",
                        keywords: question.keywords.join(", "),
                        difficulty: question.difficulty ?? "",
                        level: question.level ?? "",
                        skillId: question.skillId?.toString() ?? "",
                        templateId: question.templateId?.toString() ?? "",
                        tagIds: question.tags.map((tag) => tag.id),
                      });
                      setIsModalOpen(true);
                    }}
                    >
                      Edit
                  </Button>
                  <Button size="xs" variant="destructive" onClick={async () => {
                    try {
                      await adminCatalogService.deleteQuestion(question.id);
                      toast.success("Question deleted");
                      await load();
                    } catch {
                      toast.error("Delete failed");
                    }
                  }}>
                    Delete
                  </Button>
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>

      <Dialog open={isModalOpen} onOpenChange={(open) => (!open ? closeModal() : setIsModalOpen(open))}>
        <DialogContent className="sm:max-w-2xl">
          <DialogHeader>
            <DialogTitle>{form.id ? "Edit question" : "Create question"}</DialogTitle>
            <DialogDescription>Capture the canonical question record used for generation and curation.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-4">
            <label className="block space-y-2">
              <span className="text-sm font-medium">Question content</span>
              <textarea
                className="min-h-28 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                value={form.content}
                onChange={(event) => setForm((prev) => ({ ...prev, content: event.target.value }))}
                placeholder="Explain the tradeoffs between controlled and uncontrolled inputs in React."
                required
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Expected answer</span>
              <textarea
                className="min-h-24 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                value={form.expectedAnswer}
                onChange={(event) => setForm((prev) => ({ ...prev, expectedAnswer: event.target.value }))}
                placeholder="Look for tradeoffs in state ownership, form libraries, and validation flow."
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Keywords</span>
              <Input value={form.keywords} onChange={(event) => setForm((prev) => ({ ...prev, keywords: event.target.value }))} placeholder="state, input, form, validation" />
            </label>

            <div className="grid gap-4 md:grid-cols-2">
              <label className="space-y-2">
                <span className="text-sm font-medium">Difficulty</span>
                <Select value={form.difficulty || "none"} onValueChange={(value) => setForm((prev) => ({ ...prev, difficulty: value === "none" ? "" : value }))}>
                  <SelectTrigger className="h-10 w-full">
                    <SelectValue placeholder="Select difficulty" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="none">Select difficulty</SelectItem>
                  {difficulties.map((difficulty) => (
                    <SelectItem key={difficulty} value={difficulty}>{difficulty}</SelectItem>
                  ))}
                  </SelectContent>
                </Select>
              </label>

              <label className="space-y-2">
                <span className="text-sm font-medium">Skill</span>
                <Select value={form.skillId || "none"} onValueChange={(value) => setForm((prev) => ({ ...prev, skillId: value === "none" ? "" : value }))}>
                  <SelectTrigger className="h-10 w-full">
                    <SelectValue placeholder="Select skill" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="none">Select skill</SelectItem>
                  {flatSkills.map((skill) => (
                    <SelectItem key={skill.id} value={String(skill.id)}>
                      {"-".repeat(skill.depth)} {skill.name}
                    </SelectItem>
                  ))}
                  </SelectContent>
                </Select>
              </label>
            </div>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Template</span>
              <Select value={form.templateId || "none"} onValueChange={(value) => setForm((prev) => ({ ...prev, templateId: value === "none" ? "" : value }))}>
                <SelectTrigger className="h-10 w-full">
                  <SelectValue placeholder="Select template" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="none">Select template</SelectItem>
                {templates.map((template) => (
                  <SelectItem key={template.id} value={String(template.id)}>
                    {template.name}
                  </SelectItem>
                ))}
                </SelectContent>
              </Select>
            </label>

            <div className="space-y-3">
              <div className="text-sm font-medium">Tags</div>
              <div className="grid gap-2 md:grid-cols-2">
                {tags.map((tag) => (
                  <label key={tag.id} className="flex items-center gap-3 rounded-2xl border border-border px-3 py-2 text-sm">
                    <input type="checkbox" checked={form.tagIds.includes(tag.id)} onChange={() => toggleTag(tag.id)} />
                    <span>{tag.name}</span>
                  </label>
                ))}
              </div>
            </div>

            <DialogFooter>
              <Button type="button" variant="outline" onClick={closeModal}>Cancel</Button>
              <Button type="submit">{form.id ? "Update question" : "Create question"}</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </section>
  );
}
