import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { useLocation, useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { RefreshCw } from "lucide-react";

import { resetPasswordSchema } from "../validation/resetPasswordSchema";
import { useResetPasswordMutation, useSendPasswordResetOtpMutation } from "../api/authApi";

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { InputOTP, InputOTPGroup, InputOTPSeparator, InputOTPSlot } from "@/components/ui/input-otp";

const ResetPasswordPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const email = location.state?.email;

  const [resetPassword, { isLoading }] = useResetPasswordMutation();

  const [sendPasswordResetOtp, { isLoading: isResendingOtp }] = useSendPasswordResetOtpMutation();

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(resetPasswordSchema),
    defaultValues: {
      otp: "",
      newPassword: "",
    },
  });

  useEffect(() => {
    register("otp");
    if (!email) {
      navigate("/auth/forgot-password", { replace: true });
    }
  }, [register, email, navigate]);

  const otp = watch("otp");

  const handleResendOtp = async () => {
    try {
      const response = await sendPasswordResetOtp({
        email,
      }).unwrap();

      toast.success(response.message);
    } catch (error) {
      console.error(error);
      toast.error(error?.data?.error?.message);
    }
  };

  const onSubmit = async (data) => {
    try {
      const response = await resetPassword({
        email,
        otp: data.otp,
        newPassword: data.newPassword,
      }).unwrap();

      toast.success(response.message);
      navigate("/auth/login");
    } catch (error) {
      console.error(error);
      toast.error(error?.data?.error?.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
      <div className="flex justify-center">
        <div className="w-fit">
          <div className="mb-2.5 flex items-center justify-between">
            <span>Enter OTP</span>

            <Button type="button" variant="outline" size="xs" onClick={handleResendOtp} disabled={isResendingOtp}>
              <RefreshCw className={isResendingOtp ? "animate-spin" : ""} />
              Resend OTP
            </Button>
          </div>

          <InputOTP
            maxLength={6}
            value={otp}
            onChange={(value) =>
              setValue("otp", value, {
                shouldValidate: true,
              })
            }
          >
            <InputOTPGroup>
              <InputOTPSlot index={0} />
              <InputOTPSlot index={1} />
            </InputOTPGroup>

            <InputOTPSeparator />

            <InputOTPGroup>
              <InputOTPSlot index={2} />
              <InputOTPSlot index={3} />
            </InputOTPGroup>

            <InputOTPSeparator />

            <InputOTPGroup>
              <InputOTPSlot index={4} />
              <InputOTPSlot index={5} />
            </InputOTPGroup>
          </InputOTP>
        </div>
      </div>

      <div>
        <Input type="text" placeholder="New password" spellCheck={false} aria-invalid={!!errors.newPassword} {...register("newPassword")} />

        {errors.newPassword && <p className="mt-1 text-sm font-light text-red-500">{errors.newPassword.message}</p>}
      </div>

      <Button type="submit" className="w-full border-black" disabled={isLoading}>
        Reset Password
        {isLoading && <Spinner data-icon="inline-start" />}
      </Button>
    </form>
  );
};

export default ResetPasswordPage;
