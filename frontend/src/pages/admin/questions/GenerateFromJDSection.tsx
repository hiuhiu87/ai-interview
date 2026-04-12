import React, { useState, useEffect } from "react";
import { 
  Sparkles, 
  Loader2, 
  ChevronDown, 
  ChevronUp, 
  Save, 
  Check, 
  X, 
  Plus, 
  Trash2 
} from "lucide-react";
import { toast } from "sonner";
import { Card, CardHeader, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { 
  Select, 
  SelectContent, 
  SelectItem, 
  SelectTrigger, 
  SelectValue 
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";
import { Checkbox } from "@/components/ui/checkbox";
import { 
  Collapsible, 
  CollapsibleContent, 
  CollapsibleTrigger 
} from "@/components/ui/collapsible";
import { Input } from "@/components/ui/input";
import { useGenerateFromJD } from "./hooks/useGenerateFromJD";
import { 
  QuestionItem, 
  SkillNode, 
  TagItem, 
  TemplateItem,
  QuestionPayload 
} from "@/types/admin-catalog.types";
import { adminCatalogService } from "@/services/adminCatalogService";
import { cn } from "@/lib/utils";

interface PreviewQuestion extends QuestionItem {
  selected: boolean;
}

interface GenerateFromJDSectionProps {
  skills: SkillNode[];
  tags: TagItem[];
  templates: TemplateItem[];
  onRefresh: () => void;
}

export function GenerateFromJDSection({ 
  skills, 
  tags, 
  templates, 
  onRefresh 
}: GenerateFromJDSectionProps) {
  const [sectionOpen, setSectionOpen] = useState(false);
  const [jdInput, setJdInput] = useState("");
  const [selectedSkillId, setSelectedSkillId] = useState<number | null>(null);
  const [selectedTemplateId, setSelectedTemplateId] = useState<number | null>(null);
  const [selectedTagIds, setSelectedTagIds] = useState<number[]>([]);
  const [previewQuestions, setPreviewQuestions] = useState<PreviewQuestion[]>([]);
  const [isSaving, setIsSaving] = useState(false);

  const { generate, isGenerating, results, clearResults } = useGenerateFromJD();

  useEffect(() => {
    if (results.length > 0) {
      setPreviewQuestions(results.map(q => ({ ...q, selected: true })));
    }
  }, [results]);

  const handleGenerate = async () => {
    try {
      await generate({
        jobDescription: jdInput,
        skillId: selectedSkillId,
        templateId: selectedTemplateId,
        tagIds: selectedTagIds
      });
    } catch (error) {
      // Error handled in hook
    }
  };

  const handleSaveSelected = async () => {
    const selectedOnes = previewQuestions.filter(q => q.selected);
    if (selectedOnes.length === 0) return;

    setIsSaving(true);
    try {
      await Promise.all(selectedOnes.map(q => {
        const payload: QuestionPayload = {
          content: q.content,
          expectedAnswer: q.expectedAnswer || "",
          keywords: q.keywords,
          difficulty: q.difficulty || "medium",
          level: q.level || "senior",
          skillId: q.skillId,
          templateId: q.templateId,
          tagIds: q.tags.map(t => t.id),
          rubrics: q.rubrics.map(r => ({
            scoreLevel: r.scoreLevel,
            criteriaDescription: r.criteriaDescription
          }))
        };
        return adminCatalogService.createQuestion(payload);
      }));
      
      toast.success(`Saved ${selectedOnes.length} questions successfully`);
      onRefresh();
      setPreviewQuestions([]);
      clearResults();
      setJdInput("");
    } catch (error) {
      toast.error("Failed to save some questions");
    } finally {
      setIsSaving(false);
    }
  };

  const toggleTag = (tagId: number) => {
    setSelectedTagIds(prev => 
      prev.includes(tagId) ? prev.filter(id => id !== tagId) : [...prev, tagId]
    );
  };

  const updatePreviewQuestion = (index: number, updates: Partial<PreviewQuestion>) => {
    setPreviewQuestions(prev => prev.map((q, i) => i === index ? { ...q, ...updates } : q));
  };

  const selectAll = (val: boolean) => {
    setPreviewQuestions(prev => prev.map(q => ({ ...q, selected: val })));
  };

  return (
    <Card className="mb-6 border-2 border-indigo-100 bg-indigo-50/30 dark:border-indigo-900/30 dark:bg-indigo-950/10">
      <CardHeader className="flex flex-row items-center justify-between py-4">
        <div className="flex items-center gap-2">
          <Sparkles className="h-5 w-5 text-indigo-500" />
          <h2 className="text-lg font-semibold text-indigo-900 dark:text-indigo-100">
            Tạo câu hỏi từ Job Description
          </h2>
        </div>
        <Button 
          variant="ghost" 
          size="sm" 
          onClick={() => setSectionOpen(!sectionOpen)}
          className="hover:bg-indigo-100 dark:hover:bg-indigo-900"
        >
          {sectionOpen ? <ChevronUp className="h-5 w-5" /> : <ChevronDown className="h-5 w-5" />}
        </Button>
      </CardHeader>

      <Collapsible open={sectionOpen} onOpenChange={setSectionOpen}>
        <CollapsibleContent>
          <CardContent className="space-y-4 pb-6">
            <Textarea
              placeholder="Dán mô tả công việc vào đây..."
              className="min-h-[150px] bg-white dark:bg-zinc-950"
              value={jdInput}
              onChange={(e) => setJdInput(e.target.value)}
            />

            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
              <div className="space-y-2">
                <label className="text-sm font-medium">Skill (Optional)</label>
                <Select 
                  onValueChange={(val) => setSelectedSkillId(val === "none" ? null : Number(val))}
                  value={selectedSkillId?.toString() || "none"}
                >
                  <SelectTrigger className="bg-white dark:bg-zinc-950">
                    <SelectValue placeholder="Chọn Skill" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="none">None</SelectItem>
                    {skills.map(s => (
                      <SelectItem key={s.id} value={s.id.toString()}>{s.name}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium">Template (Optional)</label>
                <Select 
                  onValueChange={(val) => setSelectedTemplateId(val === "none" ? null : Number(val))}
                  value={selectedTemplateId?.toString() || "none"}
                >
                  <SelectTrigger className="bg-white dark:bg-zinc-950">
                    <SelectValue placeholder="Chọn Template" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="none">None</SelectItem>
                    {templates.map(t => (
                      <SelectItem key={t.id} value={t.id.toString()}>{t.name}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium">Tags</label>
              <div className="flex flex-wrap gap-2">
                {tags.map(tag => (
                  <Badge
                    key={tag.id}
                    variant={selectedTagIds.includes(tag.id) ? "default" : "outline"}
                    className={cn(
                      "cursor-pointer px-3 py-1 transition-all",
                      !selectedTagIds.includes(tag.id) && "bg-white hover:bg-zinc-100 dark:bg-zinc-950"
                    )}
                    onClick={() => toggleTag(tag.id)}
                  >
                    {tag.name}
                  </Badge>
                ))}
              </div>
            </div>

            <Button 
              className="w-full bg-indigo-600 hover:bg-indigo-700"
              disabled={!jdInput.trim() || isGenerating}
              onClick={handleGenerate}
            >
              {isGenerating ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Đang sinh câu hỏi...
                </>
              ) : (
                <>
                  <Sparkles className="mr-2 h-4 w-4" />
                  ✨ Sinh câu hỏi
                </>
              )}
            </Button>

            {previewQuestions.length > 0 && (
              <div className="mt-8 space-y-4 border-t pt-6">
                <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                  <div className="flex items-center gap-2">
                    <h3 className="text-lg font-bold">Kết quả — chọn câu muốn lưu</h3>
                    <Badge variant="secondary" className="bg-indigo-100 text-indigo-700">
                      {previewQuestions.length} câu
                    </Badge>
                  </div>
                  <div className="flex gap-2">
                    <Button 
                      variant="outline" 
                      size="sm" 
                      onClick={() => selectAll(true)}
                    >
                      Chọn tất cả
                    </Button>
                    <Button 
                      variant="outline" 
                      size="sm" 
                      onClick={() => selectAll(false)}
                    >
                      Bỏ chọn tất cả
                    </Button>
                  </div>
                </div>

                <div className="space-y-4">
                  {previewQuestions.map((q, idx) => (
                    <Card key={idx} className={cn(
                      "relative overflow-hidden border-l-4 transition-all",
                      q.difficulty === "easy" && "border-l-green-500",
                      q.difficulty === "medium" && "border-l-yellow-500",
                      q.difficulty === "hard" && "border-l-red-500",
                      !q.difficulty && "border-l-zinc-300",
                      !q.selected && "opacity-60"
                    )}>
                      <div className="absolute top-3 left-3 z-10">
                        <Checkbox 
                          checked={q.selected} 
                          onCheckedChange={(val) => updatePreviewQuestion(idx, { selected: !!val })}
                        />
                      </div>
                      <div className="pl-10 p-4 space-y-3">
                        <div className="flex flex-wrap gap-2">
                          <Badge className={cn(
                            q.difficulty === "easy" && "bg-green-100 text-green-700",
                            q.difficulty === "medium" && "bg-yellow-100 text-yellow-700",
                            q.difficulty === "hard" && "bg-red-100 text-red-700",
                          )}>
                            {q.difficulty?.toUpperCase() || "MEDIUM"}
                          </Badge>
                          <Badge variant="outline" className="bg-zinc-50">
                            {q.level?.toUpperCase() || "SENIOR"}
                          </Badge>
                        </div>

                        <div className="space-y-1">
                          <label className="text-[10px] font-bold uppercase text-zinc-500">Nội dung câu hỏi</label>
                          <Input 
                            value={q.content} 
                            onChange={(e) => updatePreviewQuestion(idx, { content: e.target.value })}
                            className="border-none bg-transparent p-0 text-base font-medium shadow-none focus-visible:ring-0"
                          />
                        </div>

                        <Collapsible>
                          <CollapsibleTrigger asChild>
                            <Button variant="ghost" size="sm" className="h-8 px-2 text-indigo-600 hover:text-indigo-700">
                              Xem Expected Answer & Rubrics
                            </Button>
                          </CollapsibleTrigger>
                          <CollapsibleContent className="mt-3 space-y-4 rounded-lg bg-zinc-50 p-4 dark:bg-zinc-900/50">
                            <div className="space-y-1">
                              <label className="text-[10px] font-bold uppercase text-zinc-500">Expected Answer</label>
                              <Textarea 
                                value={q.expectedAnswer || ""} 
                                onChange={(e) => updatePreviewQuestion(idx, { expectedAnswer: e.target.value })}
                                className="min-h-[100px] border-zinc-200 bg-white dark:border-zinc-800 dark:bg-zinc-950"
                              />
                            </div>

                            <div className="space-y-2">
                              <label className="text-[10px] font-bold uppercase text-zinc-500">Keywords</label>
                              <div className="flex flex-wrap gap-2">
                                {q.keywords.map((kw, kwIdx) => (
                                  <Badge key={kwIdx} variant="outline" className="group flex items-center gap-1 bg-white">
                                    {kw}
                                    <X 
                                      className="h-3 w-3 cursor-pointer text-zinc-400 hover:text-red-500" 
                                      onClick={() => {
                                        const newKeywords = q.keywords.filter((_, i) => i !== kwIdx);
                                        updatePreviewQuestion(idx, { keywords: newKeywords });
                                      }}
                                    />
                                  </Badge>
                                ))}
                                <Button 
                                  variant="outline" 
                                  size="icon" 
                                  className="h-6 w-6 rounded-full"
                                  onClick={() => {
                                    const kw = prompt("Thêm keyword mới:");
                                    if (kw) updatePreviewQuestion(idx, { keywords: [...q.keywords, kw] });
                                  }}
                                >
                                  <Plus className="h-3 w-3" />
                                </Button>
                              </div>
                            </div>

                            <div className="space-y-2">
                              <label className="text-[10px] font-bold uppercase text-zinc-500">Rubrics</label>
                              <div className="space-y-2">
                                {q.rubrics.map((rubric, rIdx) => (
                                  <div key={rIdx} className="flex gap-2">
                                    <Badge className="h-9 w-12 shrink-0 justify-center bg-zinc-200 text-zinc-700">
                                      {rubric.scoreLevel}
                                    </Badge>
                                    <Input 
                                      value={rubric.criteriaDescription}
                                      onChange={(e) => {
                                        const newRubrics = [...q.rubrics];
                                        newRubrics[rIdx] = { ...newRubrics[rIdx], criteriaDescription: e.target.value };
                                        updatePreviewQuestion(idx, { rubrics: newRubrics });
                                      }}
                                      className="h-9 border-zinc-200 bg-white"
                                    />
                                  </div>
                                ))}
                              </div>
                            </div>
                          </CollapsibleContent>
                        </Collapsible>
                      </div>
                    </Card>
                  ))}
                </div>

                <div className="sticky bottom-0 bg-white/80 p-4 backdrop-blur-sm dark:bg-zinc-950/80">
                  <Button 
                    className="w-full bg-indigo-600 shadow-lg hover:bg-indigo-700" 
                    size="lg"
                    disabled={isSaving || previewQuestions.filter(q => q.selected).length === 0}
                    onClick={handleSaveSelected}
                  >
                    {isSaving ? (
                      <Loader2 className="mr-2 h-5 w-5 animate-spin" />
                    ) : (
                      <Save className="mr-2 h-5 w-5" />
                    )}
                    💾 Lưu câu đã chọn ({previewQuestions.filter(q => q.selected).length})
                  </Button>
                </div>
              </div>
            )}
          </CardContent>
        </CollapsibleContent>
      </Collapsible>
    </Card>
  );
}
