import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";

import { registerSchema } from "@/features/auth/validation/registerSchema";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { useRegisterUserMutation } from "../api/authApi";
import { Spinner } from "@/components/ui/spinner";
import { toast } from "sonner";

const RegisterPage = () => {
  const [registerUser, { isLoading }] = useRegisterUserMutation();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(registerSchema),
  });

  const onSubmit = async ({ confirmPassword, ...payload }) => {
    try {
      const response = await registerUser(payload).unwrap();
      if (response.success) {
        navigate("/auth/protected");
        toast.success(response.message);
      }
    } catch (error) {
      console.error(error);
      toast.error(error.data.error.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
      <div>
        <Input type="email" placeholder="Email" spellCheck={false} aria-invalid={!!errors.email} {...register("email")} />
        {errors.email && <p className="mt-1 text-sm text-red-500 font-light">{errors.email.message}</p>}
      </div>

      <div>
        <Input type="text" placeholder="Password" spellCheck={false} aria-invalid={!!errors.password} {...register("password")} />
        {errors.password && <p className="mt-1 text-sm text-red-500 font-light">{errors.password.message}</p>}
      </div>

      {/* <div>
              <Input
                type="password"
                placeholder="Confirm password"
                spellCheck={false}
                aria-invalid={!!errors.confirmPassword}
                {...register("confirmPassword")}
              />

              {errors.confirmPassword && (
                <p className="mt-1 text-sm text-red-500">
                  {errors.confirmPassword.message}
                </p>
              )}
            </div> */}

      <Button type="submit" className="w-full border-black" disabled={isLoading}>
        Register
        {isLoading ? <Spinner data-icon="inline-start" /> : ""}
      </Button>
    </form>
  );
};

export default RegisterPage;
