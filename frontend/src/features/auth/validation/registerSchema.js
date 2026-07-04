import { z } from "zod";

export const registerSchema = z.object({
  // username: z.string().min(3, "Username must be at least 3 characters"),

  email: z.email({ message: "Invalid email format" }).transform((email) => email.toLowerCase()),

  password: z.string().min(5, "Password must be at least 5 characters"),

  // confirmPassword: z.string(),
});
// .refine((data) => data.password === data.confirmPassword, {
//   path: ["confirmPassword"],
//   message: "Passwords do not match",
// });
