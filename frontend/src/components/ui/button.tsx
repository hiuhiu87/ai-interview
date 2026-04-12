import * as React from "react"
import { cva, type VariantProps } from "class-variance-authority"
import { Slot } from "radix-ui"

import { cn } from "@/lib/utils"

const buttonVariants = cva(
  "group/button inline-flex shrink-0 items-center justify-center rounded-md border text-sm font-medium whitespace-nowrap transition-[color,background-color,border-color,box-shadow] duration-200 outline-none select-none disabled:pointer-events-none disabled:opacity-50 focus-visible:ring-4 active:translate-y-px aria-invalid:border-destructive aria-invalid:ring-4 aria-invalid:ring-destructive/15 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 cursor-pointer",
  {
    variants: {
      variant: {
        default:
          "border-[#171717] bg-[#171717] text-white shadow-[0_2px_0_rgba(23,23,23,0.12)] hover:bg-[#2a2a2a] hover:border-[#2a2a2a] focus-visible:border-[#171717] focus-visible:ring-[rgba(23,23,23,0.12)]",
        outline:
          "border-[rgba(23,23,23,0.14)] bg-white text-[#171717] shadow-[0_1px_2px_rgba(23,23,23,0.04)] hover:border-[#171717] hover:bg-[rgba(23,23,23,0.03)] focus-visible:border-[#171717] focus-visible:ring-[rgba(23,23,23,0.08)]",
        secondary:
          "border-[rgba(23,23,23,0.08)] bg-[rgba(23,23,23,0.06)] text-[#171717] hover:bg-[rgba(23,23,23,0.1)] focus-visible:border-[#171717] focus-visible:ring-[rgba(23,23,23,0.08)]",
        ghost:
          "border-transparent bg-transparent text-[rgba(23,23,23,0.72)] hover:bg-[rgba(23,23,23,0.05)] hover:text-[#171717] focus-visible:border-[#171717] focus-visible:ring-[rgba(23,23,23,0.08)]",
        destructive:
          "border-[rgba(23,23,23,0.14)] bg-white text-[#171717] shadow-[0_1px_2px_rgba(23,23,23,0.04)] hover:border-[#171717] hover:bg-[rgba(23,23,23,0.05)] focus-visible:border-[#171717] focus-visible:ring-[rgba(23,23,23,0.08)]",
        link: "text-primary underline-offset-4 hover:underline",
      },
      size: {
        default:
          "h-8 gap-1.5 px-3 has-data-[icon=inline-end]:pr-2.5 has-data-[icon=inline-start]:pl-2.5",
        xs: "h-6 gap-1 rounded-md px-2 text-xs has-data-[icon=inline-end]:pr-1.5 has-data-[icon=inline-start]:pl-1.5 [&_svg:not([class*='size-'])]:size-3",
        sm: "h-7 gap-1 rounded-md px-2.5 text-xs has-data-[icon=inline-end]:pr-2 has-data-[icon=inline-start]:pl-2 [&_svg:not([class*='size-'])]:size-3.5",
        lg: "h-10 gap-1.5 px-4 text-sm has-data-[icon=inline-end]:pr-3.5 has-data-[icon=inline-start]:pl-3.5",
        icon: "size-8",
        "icon-xs":
          "size-6 rounded-md [&_svg:not([class*='size-'])]:size-3",
        "icon-sm":
          "size-7 rounded-md",
        "icon-lg": "size-9",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
)

function Button({
  className,
  variant = "default",
  size = "default",
  asChild = false,
  ...props
}: React.ComponentProps<"button"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean
  }) {
  const Comp = asChild ? Slot.Root : "button"

  return (
    <Comp
      data-slot="button"
      data-variant={variant}
      data-size={size}
      className={cn(buttonVariants({ variant, size, className }))}
      {...props}
    />
  )
}

export { Button, buttonVariants }
