import { useState } from "react";
import { adminCatalogService } from "@/services/adminCatalogService";
import { QuestionItem } from "@/types/admin-catalog.types";
import { toast } from "sonner";

export interface GenerateFromJDRequest {
  jobDescription: string;
  skillId: number | null;
  templateId: number | null;
  tagIds: number[];
}

export const useGenerateFromJD = () => {
  const [isGenerating, setIsGenerating] = useState(false);
  const [results, setResults] = useState<QuestionItem[]>([]);

  const generate = async (payload: GenerateFromJDRequest) => {
    setIsGenerating(true);
    try {
      const data = await adminCatalogService.generateQuestionsFromJD(payload);
      setResults(data);
      return data;
    } catch (error: any) {
      const message = error.response?.data?.message || "Failed to generate questions from JD";
      toast.error(message);
      throw error;
    } finally {
      setIsGenerating(false);
    }
  };

  const clearResults = () => setResults([]);

  return {
    generate,
    isGenerating,
    results,
    clearResults,
  };
};
