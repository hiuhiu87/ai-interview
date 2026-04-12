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
import type { QuestionPayload, SkillNode, TagItem, TemplateItem } from "@/types";
import { defaultRubric, toKeywords, type QuestionFormState } from "../question-builder.utils";

type QuestionBuilderDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  form: QuestionFormState;
  onFormChange: (updater: (previous: QuestionFormState) => QuestionFormState) => void;
  difficulties: string[];
  levels: string[];
  flatSkills: Array<SkillNode & { depth: number }>;
  templates: TemplateItem[];
  tags: TagItem[];
  onSaved: () => Promise<void>;
  onClose: () => void;
};

export function QuestionBuilderDialog({
  open,
  onOpenChange,
  form,
  onFormChange,
  difficulties,
  levels,
  flatSkills,
  templates,
  tags,
  onSaved,
  onClose,
}: QuestionBuilderDialogProps) {
  const toggleTag = (tagId: number) => {
    onFormChange((previous) => ({
      ...previous,
      tagIds: previous.tagIds.includes(tagId)
        ? previous.tagIds.filter((id) => id !== tagId)
        : [...previous.tagIds, tagId],
    }));
  };

  const updateRubric = (index: number, field: "scoreLevel" | "criteriaDescription", value: string) => {
    onFormChange((previous) => ({
      ...previous,
      rubrics: previous.rubrics.map((rubric, rubricIndex) =>
        rubricIndex === index
          ? {
            ...rubric,
            [field]: field === "scoreLevel" ? Number(value) : value,
          }
          : rubric,
      ),
    }));
  };

  const addRubric = () => {
    onFormChange((previous) => ({
      ...previous,
      rubrics: [...previous.rubrics, defaultRubric(previous.rubrics.length + 1)],
    }));
  };

  const removeRubric = (index: number) => {
    onFormChange((previous) => ({
      ...previous,
      rubrics: previous.rubrics.length === 1
        ? previous.rubrics
        : previous.rubrics.filter((_, rubricIndex) => rubricIndex !== index),
    }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const normalizedRubrics = form.rubrics
      .map((rubric, index) => ({
        scoreLevel: rubric.scoreLevel || index + 1,
        criteriaDescription: rubric.criteriaDescription.trim(),
      }))
      .filter((rubric) => Boolean(rubric.criteriaDescription));

    if (!form.content.trim()) {
      toast.error("Question content is required");
      return;
    }

    if (!form.difficulty) {
      toast.error("Select a difficulty");
      return;
    }

    if (!form.level) {
      toast.error("Select a level");
      return;
    }

    if (form.tagIds.length === 0) {
      toast.error("Select at least one skill tag");
      return;
    }

    if (normalizedRubrics.length === 0) {
      toast.error("Add at least one rubric entry");
      return;
    }

    const payload: QuestionPayload = {
      content: form.content.trim(),
      expectedAnswer: form.expectedAnswer.trim() || undefined,
      keywords: toKeywords(form.keywordsInput),
      difficulty: form.difficulty,
      level: form.level,
      skillId: form.skillId ? Number(form.skillId) : null,
      templateId: form.templateId ? Number(form.templateId) : null,
      tagIds: form.tagIds,
      rubrics: normalizedRubrics,
    };

    try {
      if (form.id) {
        await adminCatalogService.updateQuestion(form.id, payload);
        toast.success("Question updated");
      } else {
        await adminCatalogService.createCmsQuestion(payload);
        toast.success("Question created");
      }
      onClose();
      await onSaved();
    } catch {
      toast.error("Could not save question");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-4xl">
        <DialogHeader>
          <DialogTitle>{form.id ? "Edit question" : "Create question"}</DialogTitle>
          <DialogDescription>
            Build the canonical question record used by HR/Admin for interview set generation.
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div className="grid gap-5 lg:grid-cols-[1.2fr_0.8fr]">
            <div className="space-y-4">
              <label className="block space-y-2">
                <span className="text-sm font-medium">Question content</span>
                <textarea
                  className="min-h-32 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                  value={form.content}
                  onChange={(event) => onFormChange((previous) => ({ ...previous, content: event.target.value }))}
                  placeholder="Explain how you would evolve a monolith into microservices."
                  required
                />
              </label>

              <label className="block space-y-2">
                <span className="text-sm font-medium">Expected answer</span>
                <textarea
                  className="min-h-28 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                  value={form.expectedAnswer}
                  onChange={(event) => onFormChange((previous) => ({ ...previous, expectedAnswer: event.target.value }))}
                  placeholder="Cover decomposition strategy, observability, data ownership, and rollout risk."
                />
              </label>

              <label className="block space-y-2">
                <span className="text-sm font-medium">Keywords</span>
                <Input
                  value={form.keywordsInput}
                  onChange={(event) => onFormChange((previous) => ({ ...previous, keywordsInput: event.target.value }))}
                  placeholder="monolith, strangler, bounded context, observability"
                />
              </label>
            </div>

            <div className="space-y-4">
              <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-1">
                <label className="space-y-2">
                  <span className="text-sm font-medium">Difficulty</span>
                  <Select value={form.difficulty || "none"} onValueChange={(value) => onFormChange((previous) => ({ ...previous, difficulty: value === "none" ? "" : value }))}>
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
                  <span className="text-sm font-medium">Level</span>
                  <Select value={form.level || "none"} onValueChange={(value) => onFormChange((previous) => ({ ...previous, level: value === "none" ? "" : value }))}>
                    <SelectTrigger className="h-10 w-full">
                      <SelectValue placeholder="Select level" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="none">Select level</SelectItem>
                      {levels.map((level) => (
                        <SelectItem key={level} value={level}>{level}</SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </label>
              </div>

              <label className="block space-y-2">
                <span className="text-sm font-medium">Primary skill</span>
                <Select value={form.skillId || "none"} onValueChange={(value) => onFormChange((previous) => ({ ...previous, skillId: value === "none" ? "" : value }))}>
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

              <label className="block space-y-2">
                <span className="text-sm font-medium">Template</span>
                <Select value={form.templateId || "none"} onValueChange={(value) => onFormChange((previous) => ({ ...previous, templateId: value === "none" ? "" : value }))}>
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
            </div>
          </div>

          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <div>
                <div className="text-sm font-medium">Skill tags</div>
                <p className="text-xs text-muted-foreground">Select the tags that drive template matching and filtering.</p>
              </div>
              <div className="text-xs text-muted-foreground">{form.tagIds.length} selected</div>
            </div>
            <div className="grid gap-2 md:grid-cols-2 xl:grid-cols-3">
              {tags.map((tag) => (
                <label key={tag.id} className="flex items-center gap-3 rounded-2xl border border-border px-3 py-2 text-sm">
                  <input type="checkbox" checked={form.tagIds.includes(tag.id)} onChange={() => toggleTag(tag.id)} />
                  <div className="min-w-0">
                    <div className="font-medium">{tag.name}</div>
                    <div className="text-xs text-muted-foreground">{tag.category || "tag"}</div>
                  </div>
                </label>
              ))}
            </div>
          </div>

          <div className="space-y-3 rounded-2xl border border-border bg-muted/30 p-4">
            <div className="flex items-center justify-between">
              <div>
                <div className="text-sm font-medium">Rubric</div>
                <p className="text-xs text-muted-foreground">Capture how interviewers should judge the answer quality.</p>
              </div>
              <Button type="button" variant="outline" size="sm" onClick={addRubric}>
                Add rubric row
              </Button>
            </div>

            <div className="space-y-3">
              {form.rubrics.map((rubric, index) => (
                <div key={`${index}-${rubric.scoreLevel}`} className="grid gap-3 rounded-2xl border border-border bg-background p-3 md:grid-cols-[120px_1fr_auto]">
                  <label className="space-y-2">
                    <span className="text-xs font-medium uppercase tracking-[0.18em] text-muted-foreground">Score level</span>
                    <Input
                      type="number"
                      min={1}
                      value={rubric.scoreLevel}
                      onChange={(event) => updateRubric(index, "scoreLevel", event.target.value)}
                    />
                  </label>
                  <label className="space-y-2">
                    <span className="text-xs font-medium uppercase tracking-[0.18em] text-muted-foreground">Criteria description</span>
                    <textarea
                      className="min-h-24 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                      value={rubric.criteriaDescription}
                      onChange={(event) => updateRubric(index, "criteriaDescription", event.target.value)}
                      placeholder="Describe what a score at this level should look like."
                    />
                  </label>
                  <div className="flex items-end">
                    <Button type="button" variant="destructive" size="xs" onClick={() => removeRubric(index)} disabled={form.rubrics.length === 1}>
                      Remove
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <DialogFooter>
            <Button type="button" variant="outline" onClick={onClose}>Cancel</Button>
            <Button type="submit">{form.id ? "Update question" : "Create question"}</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
