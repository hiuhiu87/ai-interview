import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"

import { Button } from "@/components/ui/button"

export function DemoDialog() {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button>Open</Button>
      </DialogTrigger>
      
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create User</DialogTitle>
        </DialogHeader>
        <div>Form goes here...</div>
        <div className="flex gap-2 justify-end">
          <DialogClose asChild>
            <Button className="bg-gray-600">Đóng</Button>
          </DialogClose>
          <Button>Thêm mới</Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}