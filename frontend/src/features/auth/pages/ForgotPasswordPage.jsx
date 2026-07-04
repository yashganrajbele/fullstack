import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { forgotPasswordSchema } from "../validation/forgotPasswordSchema";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { Spinner } from "@/components/ui/spinner";
import { useSendPasswordResetOtpMutation } from "../api/authApi";
import { toast } from "sonner";

const ForgotPasswordPage = () => {
  const [sendPasswordResetOtp, { isLoading }] = useSendPasswordResetOtpMutation();
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(forgotPasswordSchema),
  });

  const onSubmit = async (data) => {
    try {
      const response = await sendPasswordResetOtp(data).unwrap();
      toast.success(response.message);
      navigate("/auth/reset-password", { state: { email: data.email } });
    } catch (error) {
      console.error(error);
      toast.error(error.data.error.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
      <div>
        <Input type="text" placeholder="Email" spellCheck={false} aria-invalid={!!errors.email} {...register("email")} />
        {errors.email && <p className="mt-1 text-sm text-red-500 font-light">{errors.email.message}</p>}
      </div>

      <Button type="submit" className="w-full border-black" disabled={isLoading}>
        Get OTP
        {isLoading ? <Spinner data-icon="inline-start" /> : ""}
      </Button>
    </form>
  );
};

export default ForgotPasswordPage;
