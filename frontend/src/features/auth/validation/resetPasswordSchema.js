import { z } from "zod";

export const resetPasswordSchema = z.object({
  otp: z.string(),
  newPassword: z.string().min(5, "Password must be at least 5 characters"),
});
