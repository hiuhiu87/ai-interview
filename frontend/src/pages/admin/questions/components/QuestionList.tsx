import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import type { QuestionItem } from "@/types";

type QuestionListProps = {
  questions: QuestionItem[];
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onEdit: (question: QuestionItem) => void;
  onDelete: (questionId: string) => Promise<void>;
};

export function QuestionList({
  questions,
  page,
  totalPages,
  onPageChange,
  onEdit,
  onDelete,
}: QuestionListProps) {
  return (
    <div className="rounded-3xl border border-border bg-card p-6 shadow-sm">
      <div className="flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h2 className="text-xl font-semibold">Question inventory</h2>
          <p className="mt-1 text-sm text-muted-foreground">
            Review the normalized question dataset that powers interview set generation.
          </p>
        </div>
        <div className="text-sm text-muted-foreground">Page {page} of {totalPages}</div>
      </div>

      <div className="mt-5 overflow-hidden rounded-2xl border border-border bg-background">
        <Table>
          <TableHeader className="bg-muted/50">
            <TableRow>
              <TableHead>Question</TableHead>
              <TableHead>Difficulty</TableHead>
              <TableHead>Level</TableHead>
              <TableHead>Skill tags</TableHead>
              <TableHead>Rubric</TableHead>
              <TableHead className="w-px whitespace-nowrap text-right">Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {questions.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} className="py-8 text-center text-sm text-muted-foreground">
                  No questions match the current filters.
                </TableCell>
              </TableRow>
            ) : (
              questions.map((question) => (
                <TableRow key={question.id}>
                  <TableCell className="max-w-[380px] whitespace-normal">
                    <div className="space-y-2">
                      <div className="font-medium">{question.content}</div>
                      <div className="text-sm text-muted-foreground">
                        {question.expectedAnswer || "No expected answer"}
                      </div>
                      <div className="flex flex-wrap gap-2">
                        {question.keywords.map((keyword) => (
                          <span key={keyword} className="rounded-full border border-border px-2.5 py-1 text-xs">
                            {keyword}
                          </span>
                        ))}
                      </div>
                    </div>
                  </TableCell>
                  <TableCell className="align-top capitalize">{question.difficulty || "-"}</TableCell>
                  <TableCell className="align-top capitalize">{question.level || "-"}</TableCell>
                  <TableCell className="max-w-[240px] whitespace-normal align-top">
                    <div className="flex flex-wrap gap-2">
                      {question.skillTags.map((tag) => (
                        <span key={tag.id} className="rounded-full bg-muted px-2.5 py-1 text-xs font-medium">
                          {tag.name}
                        </span>
                      ))}
                    </div>
                  </TableCell>
                  <TableCell className="max-w-[320px] whitespace-normal align-top">
                    <div className="space-y-2">
                      {question.rubrics.map((rubric, index) => (
                        <div key={`${question.id}-${index}`} className="rounded-xl border border-border px-3 py-2 text-xs">
                          <div className="font-semibold uppercase tracking-[0.16em] text-muted-foreground">Score {rubric.scoreLevel}</div>
                          <div className="mt-1 text-foreground">{rubric.criteriaDescription}</div>
                        </div>
                      ))}
                    </div>
                  </TableCell>
                  <TableCell className="w-px whitespace-nowrap align-top">
                    <div className="flex justify-end gap-1.5">
                      <Button size="xs" variant="outline" onClick={() => onEdit(question)}>
                        Edit
                      </Button>
                      <Button size="xs" variant="destructive" onClick={() => void onDelete(question.id)}>
                        Delete
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>

      <div className="mt-4 flex items-center justify-end gap-2">
        <Button type="button" variant="outline" size="sm" onClick={() => onPageChange(page - 1)} disabled={page <= 1}>
          Previous
        </Button>
        <Button type="button" variant="outline" size="sm" onClick={() => onPageChange(page + 1)} disabled={page >= totalPages}>
          Next
        </Button>
      </div>
    </div>
  );
}
