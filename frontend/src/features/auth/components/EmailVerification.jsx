import { toast } from "sonner";
import { useSendEmailVerificationOtpMutation, useVerifyEmailMutation } from "../api/authApi";
import { useGetCurrentUserQuery } from "../api/accountManagementApi";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { verifyEmailSchema } from "../validation/verifyEmailSchema";
import { useEffect } from "react";
import { Button } from "@/components/ui/button";
import { InputOTP, InputOTPGroup, InputOTPSeparator, InputOTPSlot } from "@/components/ui/input-otp";
import { RefreshCw } from "lucide-react";
import { Spinner } from "@/components/ui/spinner";

const EmailVerification = ({ setActiveView }) => {
  const [verifyEmail, { isLoading }] = useVerifyEmailMutation();
  const [sendEmailVerificationOtp, { isLoading: isResendingOtp }] = useSendEmailVerificationOtpMutation();
  const { data: user } = useGetCurrentUserQuery();
  const email = user?.data?.email;

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(verifyEmailSchema),
    defaultValues: {
      otp: "",
      newPassword: "",
    },
  });
  const otp = watch("otp");

  useEffect(() => {
    register("otp");
  }, [register, email]);

  const handleResendOtp = async () => {
    try {
      const response = await sendEmailVerificationOtp({ email }).unwrap();
      if (response.success) {
        toast.success(response.message);
      }
    } catch (error) {
      toast.error(error?.data?.error?.message);
    }
  };

  const onSubmit = async (data) => {
    try {
      const response = await verifyEmail({ email, otp: data.otp }).unwrap();
      toast.success(response.message);
      setActiveView("HOME");
    } catch (error) {
      console.error(error);
      toast.error(error?.data?.error?.message);
    }
  };

  return (
    <>
      <h1 className="mb-2 text-lg font-extralight">Verify email</h1>
      <form className="space-y-5 h-60 max-h-60" onSubmit={handleSubmit(onSubmit)}>
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

        <Button type="submit" className="w-full border-black" disabled={isLoading}>
          Verify
          {isLoading ? <Spinner data-icon="inline-start" /> : ""}
        </Button>
      </form>
    </>
  );
};

export default EmailVerification;
