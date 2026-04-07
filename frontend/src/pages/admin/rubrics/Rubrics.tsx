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
import type { SkillNode, TagItem, TagPayload, TemplateItem, TemplatePayload } from "@/types";

type TagFormState = {
  id?: number;
  name: string;
  code: string;
  category: string;
  description: string;
};

type TemplateFormState = {
  id?: number;
  name: string;
  code: string;
  description: string;
  defaultDifficulty: string;
  skillId: string;
  tagIds: number[];
};

const defaultTagForm: TagFormState = {
  name: "",
  code: "",
  category: "",
  description: "",
};

const defaultTemplateForm: TemplateFormState = {
  name: "",
  code: "",
  description: "",
  defaultDifficulty: "",
  skillId: "",
  tagIds: [],
};

const flattenSkills = (skills: SkillNode[], depth = 0): Array<SkillNode & { depth: number }> =>
  skills.flatMap((skill) => [
    { ...skill, depth },
    ...flattenSkills(skill.children, depth + 1),
  ]);

export default function Rubrics() {
  const [skills, setSkills] = useState<SkillNode[]>([]);
  const [tags, setTags] = useState<TagItem[]>([]);
  const [templates, setTemplates] = useState<TemplateItem[]>([]);
  const [difficulties, setDifficulties] = useState<string[]>([]);
  const [tagForm, setTagForm] = useState<TagFormState>(defaultTagForm);
  const [templateForm, setTemplateForm] = useState<TemplateFormState>(defaultTemplateForm);
  const [isTagModalOpen, setIsTagModalOpen] = useState(false);
  const [isTemplateModalOpen, setIsTemplateModalOpen] = useState(false);

  const flatSkills = useMemo(() => flattenSkills(skills), [skills]);

  const load = async () => {
    try {
      const metadata = await adminCatalogService.getMetadata();
      setSkills(metadata.skills);
      setTags(metadata.tags);
      setTemplates(metadata.templates);
      setDifficulties(metadata.difficulties);
    } catch {
      toast.error("Failed to load catalog metadata");
    }
  };

  useEffect(() => {
    void load();
  }, []);

  const closeTagModal = () => {
    setIsTagModalOpen(false);
    setTagForm(defaultTagForm);
  };

  const closeTemplateModal = () => {
    setIsTemplateModalOpen(false);
    setTemplateForm(defaultTemplateForm);
  };

  const handleTagSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const payload: TagPayload = {
      name: tagForm.name,
      code: tagForm.code,
      category: tagForm.category,
      description: tagForm.description,
    };

    try {
      if (tagForm.id) {
        await adminCatalogService.updateTag(tagForm.id, payload);
        toast.success("Tag updated");
      } else {
        await adminCatalogService.createTag(payload);
        toast.success("Tag created");
      }
      closeTagModal();
      await load();
    } catch {
      toast.error("Could not save tag");
    }
  };

  const handleTemplateSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const payload: TemplatePayload = {
      name: templateForm.name,
      code: templateForm.code,
      description: templateForm.description,
      defaultDifficulty: templateForm.defaultDifficulty || undefined,
      skillId: templateForm.skillId ? Number(templateForm.skillId) : null,
      tagIds: templateForm.tagIds,
    };

    try {
      if (templateForm.id) {
        await adminCatalogService.updateTemplate(templateForm.id, payload);
        toast.success("Template updated");
      } else {
        await adminCatalogService.createTemplate(payload);
        toast.success("Template created");
      }
      closeTemplateModal();
      await load();
    } catch {
      toast.error("Could not save template");
    }
  };

  const toggleTemplateTag = (tagId: number) => {
    setTemplateForm((prev) => ({
      ...prev,
      tagIds: prev.tagIds.includes(tagId)
        ? prev.tagIds.filter((id) => id !== tagId)
        : [...prev.tagIds, tagId],
    }));
  };

  return (
    <section className="space-y-6">
      <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
        <p className="text-xs font-semibold uppercase tracking-[0.24em] text-muted-foreground">Templates & Tags</p>
        <div className="mt-3 flex flex-col gap-4 xl:flex-row xl:items-end xl:justify-between">
          <div>
            <h1 className="text-3xl font-semibold tracking-tight">Shape reusable interview blueprints and tagging rules</h1>
            <p className="mt-2 max-w-3xl text-sm leading-6 text-muted-foreground">
              Tags capture narrow facets like frameworks or behaviors. Templates package a consistent interview shape with a default difficulty, skill focus, and tag bundle.
            </p>
          </div>
          <div className="grid gap-3 sm:grid-cols-2">
            <div className="rounded-2xl bg-sky-50 px-4 py-3 text-sm text-sky-900 ring-1 ring-sky-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-sky-700">Tags</div>
              <div className="mt-1 text-2xl font-semibold">{tags.length}</div>
            </div>
            <div className="rounded-2xl bg-emerald-50 px-4 py-3 text-sm text-emerald-900 ring-1 ring-emerald-200">
              <div className="text-[11px] font-semibold uppercase tracking-[0.18em] text-emerald-700">Templates</div>
              <div className="mt-1 text-2xl font-semibold">{templates.length}</div>
            </div>
          </div>
        </div>
      </div>

      <div className="grid gap-6 2xl:grid-cols-2">
        <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold">Tag management</h2>
              <p className="mt-1 text-sm text-muted-foreground">Define searchable labels for filtering, coverage analysis, and template reuse.</p>
            </div>
            <Button onClick={() => { setTagForm(defaultTagForm); setIsTagModalOpen(true); }}>
              Add tag
            </Button>
          </div>

          <div className="mt-6 grid gap-3">
            {tags.map((tag) => (
              <div key={tag.id} className="rounded-2xl border border-border bg-background p-4">
                <div className="flex flex-col gap-3 lg:flex-row lg:items-start lg:justify-between">
                  <div className="min-w-0 flex-1">
                    <div className="font-medium">{tag.name}</div>
                    <div className="mt-1 text-xs uppercase tracking-[0.2em] text-muted-foreground">{tag.code} · {tag.category || "uncategorized"}</div>
                    <p className="mt-2 text-sm text-muted-foreground">{tag.description || "No description"}</p>
                  </div>
                  <div className="flex shrink-0 gap-1.5">
                    <Button
                      size="xs"
                      variant="outline"
                      onClick={() => {
                        setTagForm({
                          id: tag.id,
                          name: tag.name,
                          code: tag.code,
                          category: tag.category ?? "",
                          description: tag.description ?? "",
                        });
                        setIsTagModalOpen(true);
                      }}
                    >
                      Edit
                    </Button>
                    <Button size="xs" variant="destructive" onClick={async () => {
                      try {
                        await adminCatalogService.deleteTag(tag.id);
                        toast.success("Tag deleted");
                        await load();
                      } catch {
                        toast.error("Delete failed. Tag may still be referenced.");
                      }
                    }}>
                      Delete
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold">Question templates</h2>
              <p className="mt-1 text-sm text-muted-foreground">Bundle a recommended skill focus, difficulty, and tag set for faster question-set assembly.</p>
            </div>
            <Button onClick={() => { setTemplateForm(defaultTemplateForm); setIsTemplateModalOpen(true); }}>
              Add template
            </Button>
          </div>

          <div className="mt-6 grid gap-3">
            {templates.map((template) => (
              <div key={template.id} className="rounded-2xl border border-border bg-background p-4">
                <div className="flex flex-col gap-3 lg:flex-row lg:items-start lg:justify-between">
                  <div className="min-w-0 flex-1">
                    <div className="font-medium">{template.name}</div>
                    <div className="mt-1 text-xs uppercase tracking-[0.2em] text-muted-foreground">
                      {template.code} {template.skillName ? `· ${template.skillName}` : ""} {template.defaultDifficulty ? `· ${template.defaultDifficulty}` : ""}
                    </div>
                    <p className="mt-2 text-sm text-muted-foreground">{template.description || "No description"}</p>
                    <div className="mt-3 flex flex-wrap gap-2">
                      {template.tags.map((tag) => (
                        <span key={tag.id} className="rounded-full bg-muted px-3 py-1 text-xs font-medium">{tag.name}</span>
                      ))}
                    </div>
                  </div>
                  <div className="flex shrink-0 gap-1.5">
                    <Button
                      size="xs"
                      variant="outline"
                      onClick={() => {
                        setTemplateForm({
                          id: template.id,
                          name: template.name,
                          code: template.code,
                          description: template.description ?? "",
                          defaultDifficulty: template.defaultDifficulty ?? "",
                          skillId: template.skillId?.toString() ?? "",
                          tagIds: template.tags.map((tag) => tag.id),
                        });
                        setIsTemplateModalOpen(true);
                      }}
                    >
                      Edit
                    </Button>
                    <Button size="xs" variant="destructive" onClick={async () => {
                      try {
                        await adminCatalogService.deleteTemplate(template.id);
                        toast.success("Template deleted");
                        await load();
                      } catch {
                        toast.error("Delete failed. Template may still be referenced by questions.");
                      }
                    }}>
                      Delete
                    </Button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <Dialog open={isTagModalOpen} onOpenChange={(open) => (!open ? closeTagModal() : setIsTagModalOpen(open))}>
        <DialogContent className="sm:max-w-xl">
          <DialogHeader>
            <DialogTitle>{tagForm.id ? "Edit tag" : "Create tag"}</DialogTitle>
            <DialogDescription>Manage reusable labels for search, filtering, and template composition.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleTagSubmit} className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2">
              <label className="space-y-2">
                <span className="text-sm font-medium">Name</span>
                <Input value={tagForm.name} onChange={(event) => setTagForm((prev) => ({ ...prev, name: event.target.value }))} placeholder="React" required />
              </label>
              <label className="space-y-2">
                <span className="text-sm font-medium">Code</span>
                <Input value={tagForm.code} onChange={(event) => setTagForm((prev) => ({ ...prev, code: event.target.value.toUpperCase() }))} placeholder="REACT" required />
              </label>
              <label className="space-y-2">
                <span className="text-sm font-medium">Category</span>
                <Input value={tagForm.category} onChange={(event) => setTagForm((prev) => ({ ...prev, category: event.target.value }))} placeholder="framework" />
              </label>
            </div>
            <label className="block space-y-2">
              <span className="text-sm font-medium">Description</span>
              <textarea
                className="min-h-24 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                value={tagForm.description}
                onChange={(event) => setTagForm((prev) => ({ ...prev, description: event.target.value }))}
                placeholder="What does this tag represent in question curation?"
              />
            </label>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={closeTagModal}>Cancel</Button>
              <Button type="submit">{tagForm.id ? "Update tag" : "Create tag"}</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      <Dialog open={isTemplateModalOpen} onOpenChange={(open) => (!open ? closeTemplateModal() : setIsTemplateModalOpen(open))}>
        <DialogContent className="sm:max-w-2xl">
          <DialogHeader>
            <DialogTitle>{templateForm.id ? "Edit template" : "Create template"}</DialogTitle>
            <DialogDescription>Bundle difficulty, skill focus, and tags into a reusable interview template.</DialogDescription>
          </DialogHeader>
          <form onSubmit={handleTemplateSubmit} className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2">
              <label className="space-y-2">
                <span className="text-sm font-medium">Name</span>
                <Input value={templateForm.name} onChange={(event) => setTemplateForm((prev) => ({ ...prev, name: event.target.value }))} placeholder="Frontend Pairing" required />
              </label>
              <label className="space-y-2">
                <span className="text-sm font-medium">Code</span>
                <Input value={templateForm.code} onChange={(event) => setTemplateForm((prev) => ({ ...prev, code: event.target.value.toUpperCase() }))} placeholder="FE_PAIRING" required />
              </label>
              <label className="space-y-2">
                <span className="text-sm font-medium">Default difficulty</span>
                <Select value={templateForm.defaultDifficulty || "none"} onValueChange={(value) => setTemplateForm((prev) => ({ ...prev, defaultDifficulty: value === "none" ? "" : value }))}>
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
                <span className="text-sm font-medium">Primary skill</span>
                <Select value={templateForm.skillId || "none"} onValueChange={(value) => setTemplateForm((prev) => ({ ...prev, skillId: value === "none" ? "" : value }))}>
                  <SelectTrigger className="h-10 w-full">
                    <SelectValue placeholder="No primary skill" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="none">No primary skill</SelectItem>
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
              <span className="text-sm font-medium">Description</span>
              <textarea
                className="min-h-24 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                value={templateForm.description}
                onChange={(event) => setTemplateForm((prev) => ({ ...prev, description: event.target.value }))}
                placeholder="Describe the interview structure and expected candidate signal."
              />
            </label>
            <div className="space-y-3">
              <div className="text-sm font-medium">Tags</div>
              <div className="grid gap-2 md:grid-cols-2">
                {tags.map((tag) => (
                  <label key={tag.id} className="flex items-center gap-3 rounded-2xl border border-border px-3 py-2 text-sm">
                    <input type="checkbox" checked={templateForm.tagIds.includes(tag.id)} onChange={() => toggleTemplateTag(tag.id)} />
                    <span>{tag.name}</span>
                    <span className="text-xs text-muted-foreground">{tag.category || "tag"}</span>
                  </label>
                ))}
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={closeTemplateModal}>Cancel</Button>
              <Button type="submit">{templateForm.id ? "Update template" : "Create template"}</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </section>
  );
}
