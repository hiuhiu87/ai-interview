import api from "@/services/api";
import type {
  AdminMetadata,
  CommonApiResponse,
  QuestionItem,
  QuestionPayload,
  SkillNode,
  SkillPayload,
  TagItem,
  TagPayload,
  TemplateItem,
  TemplatePayload,
  UserItem,
  UserPayload,
} from "@/types/admin-metadata.types";

const adminPath = "/admin";

export const adminMetadataService = {
  getMetadata: async () => {
    const response = await api.get<CommonApiResponse<AdminMetadata>>(`${adminPath}/metadata`);
    return response.data.data;
  },
  getSkills: async () => {
    const response = await api.get<CommonApiResponse<SkillNode[]>>(`${adminPath}/skills`);
    return response.data.data;
  },
  createSkill: async (payload: SkillPayload) => {
    const response = await api.post<CommonApiResponse<SkillNode>>(`${adminPath}/skills`, payload);
    return response.data.data;
  },
  updateSkill: async (id: number, payload: SkillPayload) => {
    const response = await api.put<CommonApiResponse<SkillNode>>(`${adminPath}/skills/${id}`, payload);
    return response.data.data;
  },
  deleteSkill: async (id: number) => {
    await api.delete(`${adminPath}/skills/${id}`);
  },
  getTags: async () => {
    const response = await api.get<CommonApiResponse<TagItem[]>>(`${adminPath}/tags`);
    return response.data.data;
  },
  createTag: async (payload: TagPayload) => {
    const response = await api.post<CommonApiResponse<TagItem>>(`${adminPath}/tags`, payload);
    return response.data.data;
  },
  updateTag: async (id: number, payload: TagPayload) => {
    const response = await api.put<CommonApiResponse<TagItem>>(`${adminPath}/tags/${id}`, payload);
    return response.data.data;
  },
  deleteTag: async (id: number) => {
    await api.delete(`${adminPath}/tags/${id}`);
  },
  getTemplates: async () => {
    const response = await api.get<CommonApiResponse<TemplateItem[]>>(`${adminPath}/templates`);
    return response.data.data;
  },
  createTemplate: async (payload: TemplatePayload) => {
    const response = await api.post<CommonApiResponse<TemplateItem>>(`${adminPath}/templates`, payload);
    return response.data.data;
  },
  updateTemplate: async (id: number, payload: TemplatePayload) => {
    const response = await api.put<CommonApiResponse<TemplateItem>>(`${adminPath}/templates/${id}`, payload);
    return response.data.data;
  },
  deleteTemplate: async (id: number) => {
    await api.delete(`${adminPath}/templates/${id}`);
  },
  getQuestions: async () => {
    const response = await api.get<CommonApiResponse<QuestionItem[]>>(`${adminPath}/questions`);
    return response.data.data;
  },
  createQuestion: async (payload: QuestionPayload) => {
    const response = await api.post<CommonApiResponse<QuestionItem>>(`${adminPath}/questions`, payload);
    return response.data.data;
  },
  updateQuestion: async (id: string, payload: QuestionPayload) => {
    const response = await api.put<CommonApiResponse<QuestionItem>>(`${adminPath}/questions/${id}`, payload);
    return response.data.data;
  },
  deleteQuestion: async (id: string) => {
    await api.delete(`${adminPath}/questions/${id}`);
  },
  getUsers: async () => {
    const response = await api.get<CommonApiResponse<UserItem[]>>(`${adminPath}/users`);
    return response.data.data;
  },
  createUser: async (payload: UserPayload) => {
    const response = await api.post<CommonApiResponse<UserItem>>(`${adminPath}/users`, payload);
    return response.data.data;
  },
  updateUser: async (id: string, payload: UserPayload) => {
    const response = await api.put<CommonApiResponse<UserItem>>(`${adminPath}/users/${id}`, payload);
    return response.data.data;
  },
  deleteUser: async (id: string) => {
    await api.delete(`${adminPath}/users/${id}`);
  },
};
