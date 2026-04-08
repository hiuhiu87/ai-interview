export type SkillNode = {
  id: number;
  name: string;
  code: string;
  description?: string | null;
  displayOrder?: number | null;
  parentId?: number | null;
  children: SkillNode[];
};

export type TagItem = {
  id: number;
  name: string;
  code: string;
  category?: string | null;
  description?: string | null;
};

export type TemplateItem = {
  id: number;
  name: string;
  code: string;
  description?: string | null;
  defaultDifficulty?: string | null;
  skillId?: number | null;
  skillName?: string | null;
  tags: TagItem[];
};

export type QuestionItem = {
  id: string;
  content: string;
  expectedAnswer?: string | null;
  keywords: string[];
  difficulty?: string | null;
  skillId?: number | null;
  skillName?: string | null;
  templateId?: number | null;
  templateName?: string | null;
  tags: TagItem[];
};

export type CatalogMetadata = {
  difficulties: string[];
  skills: SkillNode[];
  tags: TagItem[];
  templates: TemplateItem[];
};

export type CommonApiResponse<T> = {
  data: T;
  request_id: string;
  message: string;
  code: number;
  server_time: number;
};

export type SkillPayload = {
  name: string;
  code: string;
  description?: string;
  displayOrder?: number;
  parentId?: number | null;
};

export type TagPayload = {
  name: string;
  code: string;
  category?: string;
  description?: string;
};

export type TemplatePayload = {
  name: string;
  code: string;
  description?: string;
  defaultDifficulty?: string;
  skillId?: number | null;
  tagIds: number[];
};

export type QuestionPayload = {
  content: string;
  expectedAnswer?: string;
  keywords: string[];
  difficulty?: string;
  skillId?: number | null;
  templateId?: number | null;
  tagIds: number[];
};
