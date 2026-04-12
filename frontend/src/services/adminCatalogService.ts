import api from "@/services/api";
import type {
  CatalogMetadata,
  CommonApiResponse,
  GenerateQuestionSetPayload,
  QuestionItem,
  QuestionPayload,
  QuestionSetResponse,
  SkillNode,
  SkillPayload,
  TagItem,
  TagPayload,
  TemplateItem,
  TemplatePayload,
} from "@/types/admin-catalog.types";

const basePath = "/admin/catalog";

export const adminCatalogService = {
  getCmsQuestions: async () => {
    const response = await api.get<CommonApiResponse<QuestionItem[]>>("/cms/questions");
    return response.data.data;
  },
  createCmsQuestion: async (payload: QuestionPayload) => {
    const response = await api.post<CommonApiResponse<QuestionItem>>("/cms/questions", payload);
    return response.data.data;
  },
  generateQuestionSet: async (payload: GenerateQuestionSetPayload) => {
    const response = await api.post<CommonApiResponse<QuestionSetResponse>>("/cms/question-set/generate", payload);
    return response.data.data;
  },
  getMetadata: async () => {
    const response = await api.get<CommonApiResponse<CatalogMetadata>>(`${basePath}/metadata`);
    return response.data.data;
  },
  getSkills: async () => {
    const response = await api.get<CommonApiResponse<SkillNode[]>>(`${basePath}/skills`);
    return response.data.data;
  },
  createSkill: async (payload: SkillPayload) => {
    const response = await api.post<CommonApiResponse<SkillNode>>(`${basePath}/skills`, payload);
    return response.data.data;
  },
  updateSkill: async (id: number, payload: SkillPayload) => {
    const response = await api.put<CommonApiResponse<SkillNode>>(`${basePath}/skills/${id}`, payload);
    return response.data.data;
  },
  deleteSkill: async (id: number) => {
    await api.delete(`${basePath}/skills/${id}`);
  },
  getTags: async () => {
    const response = await api.get<CommonApiResponse<TagItem[]>>(`${basePath}/tags`);
    return response.data.data;
  },
  createTag: async (payload: TagPayload) => {
    const response = await api.post<CommonApiResponse<TagItem>>(`${basePath}/tags`, payload);
    return response.data.data;
  },
  updateTag: async (id: number, payload: TagPayload) => {
    const response = await api.put<CommonApiResponse<TagItem>>(`${basePath}/tags/${id}`, payload);
    return response.data.data;
  },
  deleteTag: async (id: number) => {
    await api.delete(`${basePath}/tags/${id}`);
  },
  getTemplates: async () => {
    const response = await api.get<CommonApiResponse<TemplateItem[]>>(`${basePath}/templates`);
    return response.data.data;
  },
  createTemplate: async (payload: TemplatePayload) => {
    const response = await api.post<CommonApiResponse<TemplateItem>>(`${basePath}/templates`, payload);
    return response.data.data;
  },
  updateTemplate: async (id: number, payload: TemplatePayload) => {
    const response = await api.put<CommonApiResponse<TemplateItem>>(`${basePath}/templates/${id}`, payload);
    return response.data.data;
  },
  deleteTemplate: async (id: number) => {
    await api.delete(`${basePath}/templates/${id}`);
  },
  getQuestions: async () => {
    const response = await api.get<CommonApiResponse<QuestionItem[]>>(`${basePath}/questions`);
    return response.data.data;
  },
  createQuestion: async (payload: QuestionPayload) => {
    const response = await api.post<CommonApiResponse<QuestionItem>>(`${basePath}/questions`, payload);
    return response.data.data;
  },
  updateQuestion: async (id: string, payload: QuestionPayload) => {
    const response = await api.put<CommonApiResponse<QuestionItem>>(`${basePath}/questions/${id}`, payload);
    return response.data.data;
  },
  deleteQuestion: async (id: string) => {
    await api.delete(`${basePath}/questions/${id}`);
  },
  generateQuestionsFromJD: async (payload: {
    jobDescription: string;
    skillId: number | null;
    templateId: number | null;
    tagIds: number[];
  }) => {
    const response = await api.post<CommonApiResponse<QuestionItem[]>>(
      "/cms/questions/generate-from-jd",
      payload
    );
    return response.data.data;
  },
};
