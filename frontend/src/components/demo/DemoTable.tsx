import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
function DemoTable() {
  
  type User = {
    id: string
    name: string
    email: string
    role: string
  }

  const users: User[] = [
    { id: "1", name: "Nguyen Van A", email: "a@gmail.com", role: "Admin" },
    { id: "2", name: "Tran Van B", email: "b@gmail.com", role: "User" },
    { id: "3", name: "Le Van C", email: "c@gmail.com", role: "User" },
  ]
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>ID</TableHead>
          <TableHead>Name</TableHead>
          <TableHead>Email</TableHead>
          <TableHead>Role</TableHead>
        </TableRow>
      </TableHeader>

      <TableBody>
        {users.map((user) => (
          <TableRow key={user.id}>
            <TableCell>{user.id}</TableCell>
            <TableCell className="font-medium">
              {user.name}
            </TableCell>
            <TableCell>{user.email}</TableCell>
            <TableCell>{user.role}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}

export default DemoTable

