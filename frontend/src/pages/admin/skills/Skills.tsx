import type { ReactNode } from "react";
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
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { adminMetadataService } from "@/services/adminMetadataService";
import type { SkillNode, SkillPayload } from "@/types";

type SkillFormState = {
  id?: number;
  name: string;
  code: string;
  description: string;
  displayOrder: string;
  parentId: string;
};

const defaultForm: SkillFormState = {
  name: "",
  code: "",
  description: "",
  displayOrder: "",
  parentId: "",
};

const flattenSkills = (skills: SkillNode[], depth = 0): Array<SkillNode & { depth: number }> =>
  skills.flatMap((skill) => [
    { ...skill, depth },
    ...flattenSkills(skill.children, depth + 1),
  ]);

const renderTreeRows = (
  skills: SkillNode[],
  onEdit: (skill: SkillNode) => void,
  onDelete: (id: number) => void,
  depth = 0,
): ReactNode[] =>
  skills.flatMap((skill) => [
    <TableRow key={skill.id}>
      <TableCell>
        <div className="flex items-center gap-2" style={{ paddingLeft: `${depth * 18}px` }}>
          <span className="text-muted-foreground">{depth > 0 ? "└" : "•"}</span>
          <div>
            <div className="font-medium">{skill.name}</div>
            <div className="text-xs text-muted-foreground">{skill.code}</div>
          </div>
        </div>
      </TableCell>
      <TableCell className="text-sm text-muted-foreground">{skill.description || "-"}</TableCell>
      <TableCell className="text-sm">{skill.displayOrder ?? "-"}</TableCell>
      <TableCell className="w-px whitespace-nowrap text-right">
        <div className="flex justify-end gap-1.5">
          <Button size="xs" variant="outline" onClick={() => onEdit(skill)}>
            Edit
          </Button>
          <Button size="xs" variant="destructive" onClick={() => onDelete(skill.id)}>
            Delete
          </Button>
        </div>
      </TableCell>
    </TableRow>,
    ...renderTreeRows(skill.children, onEdit, onDelete, depth + 1),
  ]);

export default function Skills() {
  const [skills, setSkills] = useState<SkillNode[]>([]);
  const [form, setForm] = useState<SkillFormState>(defaultForm);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const flatSkills = useMemo(() => flattenSkills(skills), [skills]);

  const loadSkills = async () => {
    try {
      setSkills(await adminMetadataService.getSkills());
    } catch {
      toast.error("Failed to load skill tree");
    }
  };

  useEffect(() => {
    void loadSkills();
  }, []);

  const resetForm = () => setForm(defaultForm);

  const closeModal = () => {
    setIsModalOpen(false);
    resetForm();
  };

  const openCreateModal = () => {
    resetForm();
    setIsModalOpen(true);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);

    const payload: SkillPayload = {
      name: form.name,
      code: form.code,
      description: form.description,
      displayOrder: form.displayOrder ? Number(form.displayOrder) : undefined,
      parentId: form.parentId ? Number(form.parentId) : null,
    };

    try {
      if (form.id) {
        await adminMetadataService.updateSkill(form.id, payload);
        toast.success("Skill updated");
      } else {
        await adminMetadataService.createSkill(payload);
        toast.success("Skill created");
      }
      closeModal();
      await loadSkills();
    } catch {
      toast.error("Could not save skill");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEdit = (skill: SkillNode) => {
    setForm({
      id: skill.id,
      name: skill.name,
      code: skill.code,
      description: skill.description ?? "",
      displayOrder: skill.displayOrder?.toString() ?? "",
      parentId: skill.parentId?.toString() ?? "",
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await adminMetadataService.deleteSkill(id);
      toast.success("Skill deleted");
      if (form.id === id) {
        closeModal();
      }
      await loadSkills();
    } catch {
      toast.error("Delete failed. Check child skills or references first.");
    }
  };

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 rounded-3xl border border-slate-200 bg-white p-6 shadow-sm lg:flex-row lg:items-end lg:justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-[0.24em] text-slate-400">CMS</p>
          <h1 className="mt-2 text-2xl font-semibold text-slate-950">Skill Tree</h1>
          <p className="mt-1 text-sm text-slate-500">Manage the skill tree used by templates and questions.</p>
        </div>
        <div className="flex items-center gap-3">
          <div className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700">
            {flatSkills.length} skills
          </div>
          <Button onClick={openCreateModal}>Add skill</Button>
        </div>
      </div>

      <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
        <div>
          <h2 className="text-lg font-semibold text-slate-950">Skill list</h2>
          <p className="mt-1 text-sm text-slate-500">Choose a parent to build the tree.</p>
        </div>

        <div className="mt-5 overflow-hidden rounded-2xl border border-slate-200 bg-white">
          <Table>
            <TableHeader className="bg-slate-50">
              <TableRow>
                <TableHead>Skill</TableHead>
                <TableHead>Description</TableHead>
                <TableHead>Order</TableHead>
                <TableHead className="w-px whitespace-nowrap text-right">Action</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>{renderTreeRows(skills, handleEdit, handleDelete)}</TableBody>
          </Table>
        </div>
      </div>

      <Dialog open={isModalOpen} onOpenChange={(open) => (!open ? closeModal() : setIsModalOpen(open))}>
        <DialogContent className="sm:max-w-xl">
          <DialogHeader>
            <DialogTitle>{form.id ? "Edit skill" : "Create skill"}</DialogTitle>
            <DialogDescription>Skill details.</DialogDescription>
          </DialogHeader>

          <form onSubmit={handleSubmit} className="space-y-4">
            <label className="block space-y-2">
              <span className="text-sm font-medium">Skill name</span>
              <Input
                value={form.name}
                onChange={(event) => setForm((prev) => ({ ...prev, name: event.target.value }))}
                placeholder="Frontend"
                required
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Code</span>
              <Input
                value={form.code}
                onChange={(event) => setForm((prev) => ({ ...prev, code: event.target.value.toUpperCase() }))}
                placeholder="FRONTEND"
                required
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Parent skill</span>
              <Select
                value={form.parentId || "root"}
                onValueChange={(value) => setForm((prev) => ({ ...prev, parentId: value === "root" ? "" : value }))}
              >
                <SelectTrigger className="h-10 w-full">
                  <SelectValue placeholder="Root skill" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="root">Root skill</SelectItem>
                  {flatSkills
                    .filter((skill) => skill.id !== form.id)
                    .map((skill) => (
                      <SelectItem key={skill.id} value={String(skill.id)}>
                        {"-".repeat(skill.depth)} {skill.name}
                      </SelectItem>
                    ))}
                </SelectContent>
              </Select>
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Display order</span>
              <Input
                type="number"
                value={form.displayOrder}
                onChange={(event) => setForm((prev) => ({ ...prev, displayOrder: event.target.value }))}
                placeholder="10"
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Description</span>
              <textarea
                className="min-h-28 w-full rounded-xl border border-input bg-background px-3 py-2 text-sm outline-none"
                value={form.description}
                onChange={(event) => setForm((prev) => ({ ...prev, description: event.target.value }))}
                placeholder="Description"
              />
            </label>

            <DialogFooter>
              <Button type="button" variant="outline" onClick={closeModal}>
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Saving..." : form.id ? "Update skill" : "Create skill"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </section>
  );
}
