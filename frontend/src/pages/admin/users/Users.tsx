import { useEffect, useState } from "react";
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
import type { UserItem, UserPayload } from "@/types";

type UserFormState = {
  id?: string;
  fullName: string;
  email: string;
  role: "ADMIN" | "INTERVIEWER";
};

const defaultForm: UserFormState = {
  fullName: "",
  email: "",
  role: "ADMIN",
};

const accessLabelByRole: Record<UserItem["role"], string> = {
  ADMIN: "CMS",
  INTERVIEWER: "Interview Workspace",
};

export default function Users() {
  const [users, setUsers] = useState<UserItem[]>([]);
  const [form, setForm] = useState<UserFormState>(defaultForm);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const loadUsers = async () => {
    try {
      setUsers(await adminMetadataService.getUsers());
    } catch {
      toast.error("Failed to load users");
    }
  };

  useEffect(() => {
    void loadUsers();
  }, []);

  const closeModal = () => {
    setForm(defaultForm);
    setIsModalOpen(false);
  };

  const openCreateModal = () => {
    setForm(defaultForm);
    setIsModalOpen(true);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);

    const payload: UserPayload = {
      fullName: form.fullName,
      email: form.email,
      role: form.role,
    };

    try {
      if (form.id) {
        await adminMetadataService.updateUser(form.id, payload);
        toast.success("User updated");
      } else {
        await adminMetadataService.createUser(payload);
        toast.success("User created");
      }
      closeModal();
      await loadUsers();
    } catch {
      toast.error("Could not save user");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await adminMetadataService.deleteUser(id);
      toast.success("User deleted");
      await loadUsers();
    } catch {
      toast.error("Delete failed");
    }
  };

  return (
    <section className="space-y-6">
      <div className="flex flex-col gap-4 rounded-3xl border border-slate-200 bg-white p-6 shadow-sm lg:flex-row lg:items-end lg:justify-between">
        <div>
          <p className="text-xs font-semibold uppercase tracking-[0.24em] text-slate-400">CMS</p>
          <h1 className="mt-2 text-2xl font-semibold text-slate-950">Users</h1>
          <p className="mt-1 text-sm text-slate-500">Manage admin and interviewer accounts.</p>
        </div>
        <div className="flex items-center gap-3">
          <div className="rounded-full bg-slate-100 px-4 py-2 text-sm font-medium text-slate-700">
            {users.length} users
          </div>
          <Button onClick={openCreateModal}>Add user</Button>
        </div>
      </div>

      <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
        <div>
          <h2 className="text-lg font-semibold text-slate-950">User list</h2>
          <p className="mt-1 text-sm text-slate-500">Admins access CMS. Interviewers access the interview workspace.</p>
        </div>

        <div className="mt-5 overflow-hidden rounded-2xl border border-slate-200 bg-white">
          <Table>
            <TableHeader className="bg-slate-50">
              <TableRow>
                <TableHead>Full name</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Role</TableHead>
                <TableHead>Access</TableHead>
                <TableHead className="w-px whitespace-nowrap text-right">Action</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell className="font-medium">{user.fullName}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.role}</TableCell>
                  <TableCell>{accessLabelByRole[user.role]}</TableCell>
                  <TableCell className="w-px whitespace-nowrap text-right">
                    <div className="flex justify-end gap-1.5">
                      <Button
                        size="xs"
                        variant="outline"
                        onClick={() => {
                          setForm({
                            id: user.id,
                            fullName: user.fullName,
                            email: user.email,
                            role: user.role,
                          });
                          setIsModalOpen(true);
                        }}
                      >
                        Edit
                      </Button>
                      <Button size="xs" variant="destructive" onClick={() => void handleDelete(user.id)}>
                        Delete
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </div>

      <Dialog open={isModalOpen} onOpenChange={(open) => (!open ? closeModal() : setIsModalOpen(open))}>
        <DialogContent className="sm:max-w-xl">
          <DialogHeader>
            <DialogTitle>{form.id ? "Edit user" : "Create user"}</DialogTitle>
            <DialogDescription>User details.</DialogDescription>
          </DialogHeader>

          <form onSubmit={handleSubmit} className="space-y-4">
            <label className="block space-y-2">
              <span className="text-sm font-medium">Full name</span>
              <Input
                value={form.fullName}
                onChange={(event) => setForm((prev) => ({ ...prev, fullName: event.target.value }))}
                placeholder="Alice Admin"
                required
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Email</span>
              <Input
                type="email"
                value={form.email}
                onChange={(event) => setForm((prev) => ({ ...prev, email: event.target.value }))}
                placeholder="alice.admin@example.com"
                required
              />
            </label>

            <label className="block space-y-2">
              <span className="text-sm font-medium">Role</span>
              <Select
                value={form.role}
                onValueChange={(value) => setForm((prev) => ({ ...prev, role: value as UserItem["role"] }))}
              >
                <SelectTrigger className="h-10 w-full">
                  <SelectValue placeholder="Select role" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ADMIN">ADMIN</SelectItem>
                  <SelectItem value="INTERVIEWER">INTERVIEWER</SelectItem>
                </SelectContent>
              </Select>
            </label>

            <div className="rounded-2xl bg-muted px-4 py-3 text-sm text-muted-foreground">
              Access: {accessLabelByRole[form.role]}
            </div>

            <DialogFooter>
              <Button type="button" variant="outline" onClick={closeModal}>
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? "Saving..." : form.id ? "Update user" : "Create user"}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </section>
  );
}
