import { useEffect, useMemo, useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { adminCatalogService } from "@/services/adminCatalogService";
import type { QuestionItem, SkillNode, TagItem, TemplateItem } from "@/types";
import { QuestionBuilderDialog } from "./components/QuestionBuilderDialog";
import { QuestionFilters } from "./components/QuestionFilters";
import { QuestionList } from "./components/QuestionList";
import { GenerateFromJDSection } from "./GenerateFromJDSection";
import {
  defaultFilters,
  defaultQuestionForm,
  flattenSkills,
  toQuestionForm,
  type QuestionFiltersState,
  type QuestionFormState,
} from "./question-builder.utils";

const PAGE_SIZE = 6;

export default function QuestionBuilderPage() {
  const [questions, setQuestions] = useState<QuestionItem[]>([]);
  const [skills, setSkills] = useState<SkillNode[]>([]);
  const [tags, setTags] = useState<TagItem[]>([]);
  const [templates, setTemplates] = useState<TemplateItem[]>([]);
  const [difficulties, setDifficulties] = useState<string[]>([]);
  const [levels, setLevels] = useState<string[]>([]);
  const [filters, setFilters] = useState<QuestionFiltersState>(defaultFilters);
  const [form, setForm] = useState<QuestionFormState>(defaultQuestionForm);
  const [currentPage, setCurrentPage] = useState(1);
  const [isBuilderOpen, setIsBuilderOpen] = useState(false);

  const flatSkills = useMemo(() => flattenSkills(skills), [skills]);

  const load = async () => {
    try {
      const [metadata, questionItems] = await Promise.all([
        adminCatalogService.getMetadata(),
        adminCatalogService.getCmsQuestions(),
      ]);
      setSkills(metadata.skills);
      setTags(metadata.tags);
      setTemplates(metadata.templates);
      setDifficulties(metadata.difficulties);
      setLevels(metadata.levels);
      setQuestions(questionItems);
    } catch {
      toast.error("Failed to load question builder data");
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const filteredQuestions = useMemo(() => (
    questions.filter((question) => {
      const matchesDifficulty = filters.difficulty === "all" || question.difficulty === filters.difficulty;
      const matchesTag = filters.tagId === "all" || question.skillTags.some((tag) => tag.id === Number(filters.tagId));
      return matchesDifficulty && matchesTag;
    })
  ), [filters, questions]);

  const paginatedQuestions = useMemo(() => {
    const startIndex = (currentPage - 1) * PAGE_SIZE;
    return filteredQuestions.slice(startIndex, startIndex + PAGE_SIZE);
  }, [currentPage, filteredQuestions]);

  const totalPages = Math.max(1, Math.ceil(filteredQuestions.length / PAGE_SIZE));

  useEffect(() => {
    if (currentPage > totalPages) {
      setCurrentPage(totalPages);
    }
  }, [currentPage, totalPages]);

  const closeBuilder = () => {
    setIsBuilderOpen(false);
    setForm(defaultQuestionForm);
  };

  const handleDelete = async (questionId: string) => {
    try {
      await adminCatalogService.deleteQuestion(questionId);
      toast.success("Question deleted");
      await load();
    } catch {
      toast.error("Delete failed");
    }
  };

  return (
    <section className="space-y-6">
      <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
        <p className="text-xs font-semibold uppercase tracking-[0.24em] text-muted-foreground">Question Builder</p>
        <div className="mt-3 flex flex-col gap-4 xl:flex-row xl:items-end xl:justify-between">
          <div>
            <h1 className="text-3xl font-semibold tracking-tight">Create structured questions and generate interview-ready sets</h1>
            <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
              Each question now carries expected answer guidance, keywords, level, skill tags, and rubric criteria so templates can assemble a reliable interview set.
            </p>
          </div>
          <div className="grid gap-3 sm:grid-cols-3">
            <div className="rounded-2xl bg-rose-50 px-4 py-3 text-sm text-rose-900 ring-1 ring-rose-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-rose-700">Questions</div>
              <div className="mt-1 text-2xl font-semibold">{questions.length}</div>
            </div>
            <div className="rounded-2xl bg-indigo-50 px-4 py-3 text-sm text-indigo-900 ring-1 ring-indigo-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-indigo-700">Difficulties</div>
              <div className="mt-1 capitalize">{difficulties.join(", ")}</div>
            </div>
            <div className="rounded-2xl bg-amber-50 px-4 py-3 text-sm text-amber-900 ring-1 ring-amber-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-amber-700">Levels</div>
              <div className="mt-1 capitalize">{levels.join(", ")}</div>
            </div>
          </div>
        </div>
        <div className="mt-6 flex flex-wrap gap-3">
          <Button onClick={() => { setForm(defaultQuestionForm); setIsBuilderOpen(true); }}>
            Add question
          </Button>
        </div>
      </div>

      <GenerateFromJDSection
        skills={skills}
        tags={tags}
        templates={templates}
        onRefresh={load}
      />

      <QuestionFilters
        filters={filters}
        onFiltersChange={(next) => {
          setFilters(next);
          setCurrentPage(1);
        }}
        difficulties={difficulties}
        tags={tags}
      />

      <QuestionList
        questions={paginatedQuestions}
        page={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        onEdit={(question) => {
          setForm(toQuestionForm(question));
          setIsBuilderOpen(true);
        }}
        onDelete={handleDelete}
      />

      <QuestionBuilderDialog
        open={isBuilderOpen}
        onOpenChange={(open) => (!open ? closeBuilder() : setIsBuilderOpen(open))}
        form={form}
        onFormChange={(updater) => setForm((previous) => updater(previous))}
        difficulties={difficulties}
        levels={levels}
        flatSkills={flatSkills}
        templates={templates}
        tags={tags}
        onSaved={load}
        onClose={closeBuilder}
      />
    </section>
  );
}
