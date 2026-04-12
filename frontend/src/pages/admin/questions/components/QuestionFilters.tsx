import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import type { TagItem } from "@/types";
import type { QuestionFiltersState } from "../question-builder.utils";

type QuestionFiltersProps = {
  filters: QuestionFiltersState;
  onFiltersChange: (next: QuestionFiltersState) => void;
  difficulties: string[];
  tags: TagItem[];
};

export function QuestionFilters({ filters, onFiltersChange, difficulties, tags }: QuestionFiltersProps) {
  return (
    <div className="grid gap-4 rounded-3xl border border-border bg-card p-5 shadow-sm lg:grid-cols-[1fr_1fr_auto]">
      <label className="space-y-2">
        <span className="text-sm font-medium">Difficulty filter</span>
        <Select value={filters.difficulty} onValueChange={(value) => onFiltersChange({ ...filters, difficulty: value })}>
          <SelectTrigger className="h-10 w-full">
            <SelectValue placeholder="All difficulties" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All difficulties</SelectItem>
            {difficulties.map((difficulty) => (
              <SelectItem key={difficulty} value={difficulty}>{difficulty}</SelectItem>
            ))}
          </SelectContent>
        </Select>
      </label>

      <label className="space-y-2">
        <span className="text-sm font-medium">Skill tag filter</span>
        <Select value={filters.tagId} onValueChange={(value) => onFiltersChange({ ...filters, tagId: value })}>
          <SelectTrigger className="h-10 w-full">
            <SelectValue placeholder="All tags" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All tags</SelectItem>
            {tags.map((tag) => (
              <SelectItem key={tag.id} value={String(tag.id)}>{tag.name}</SelectItem>
            ))}
          </SelectContent>
        </Select>
      </label>

      <div className="flex items-end">
        <Button type="button" variant="outline" onClick={() => onFiltersChange({ difficulty: "all", tagId: "all" })}>
          Reset filters
        </Button>
      </div>
    </div>
  );
}
