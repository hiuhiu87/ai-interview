import type { QuestionItem, RubricItem, SkillNode } from "@/types";

export type QuestionFormState = {
  id?: string;
  content: string;
  expectedAnswer: string;
  keywordsInput: string;
  difficulty: string;
  level: string;
  skillId: string;
  templateId: string;
  tagIds: number[];
  rubrics: RubricItem[];
};

export type QuestionFiltersState = {
  difficulty: string;
  tagId: string;
};

export type GeneratorFormState = {
  templateId: string;
  easy: string;
  medium: string;
  hard: string;
};

export const defaultRubric = (scoreLevel: number = 1): RubricItem => ({
  scoreLevel,
  criteriaDescription: "",
});

export const defaultQuestionForm: QuestionFormState = {
  content: "",
  expectedAnswer: "",
  keywordsInput: "",
  difficulty: "",
  level: "",
  skillId: "",
  templateId: "",
  tagIds: [],
  rubrics: [defaultRubric()],
};

export const defaultFilters: QuestionFiltersState = {
  difficulty: "all",
  tagId: "all",
};

export const defaultGeneratorForm: GeneratorFormState = {
  templateId: "",
  easy: "3",
  medium: "4",
  hard: "3",
};

export const flattenSkills = (skills: SkillNode[], depth = 0): Array<SkillNode & { depth: number }> =>
  skills.flatMap((skill) => [
    { ...skill, depth },
    ...flattenSkills(skill.children, depth + 1),
  ]);

export const toKeywords = (keywordsInput: string) =>
  keywordsInput
    .split(",")
    .map((keyword) => keyword.trim())
    .filter(Boolean);

export const toQuestionForm = (question: QuestionItem): QuestionFormState => ({
  id: question.id,
  content: question.content,
  expectedAnswer: question.expectedAnswer ?? "",
  keywordsInput: question.keywords.join(", "),
  difficulty: question.difficulty ?? "",
  level: question.level ?? "",
  skillId: question.skillId?.toString() ?? "",
  templateId: question.templateId?.toString() ?? "",
  tagIds: question.skillTags.map((tag) => tag.id),
  rubrics: question.rubrics.length > 0
    ? question.rubrics.map((rubric) => ({
      scoreLevel: rubric.scoreLevel,
      criteriaDescription: rubric.criteriaDescription,
    }))
    : [defaultRubric()],
});
