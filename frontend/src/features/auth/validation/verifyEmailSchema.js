import { z } from "zod";

export const verifyEmailSchema = z.object({
  otp: z.string(),
});
