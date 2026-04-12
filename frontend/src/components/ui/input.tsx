import * as React from "react"

import { cn } from "@/lib/utils"

function Input({ className, type, ...props }: React.ComponentProps<"input">) {
  return (
    <input
      type={type}
      data-slot="input"
      className={cn(
        "h-8 w-full min-w-0 rounded-md border border-[rgba(23,23,23,0.14)] bg-white px-3 py-1.5 text-sm text-[#171717] shadow-[0_1px_2px_rgba(23,23,23,0.04)] transition-[border-color,box-shadow,background-color] outline-none file:inline-flex file:h-6 file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-[#171717] placeholder:text-[rgba(23,23,23,0.4)] focus-visible:border-[#171717] focus-visible:ring-4 focus-visible:ring-[rgba(23,23,23,0.08)] disabled:pointer-events-none disabled:cursor-not-allowed disabled:bg-[rgba(23,23,23,0.04)] disabled:text-[rgba(23,23,23,0.42)] disabled:opacity-100 aria-invalid:border-[#171717] aria-invalid:ring-4 aria-invalid:ring-[rgba(23,23,23,0.08)] dark:border-input dark:bg-input/30 dark:text-foreground dark:placeholder:text-muted-foreground dark:disabled:bg-input/70",
        className
      )}
      {...props}
    />
  )
}

export { Input }
