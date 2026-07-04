import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

import { useLoginMutation } from "@/features/auth/api/authApi";
import { loginSchema } from "@/features/auth/validation/loginSchema";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Spinner } from "@/components/ui/spinner";
import { useEffect } from "react";

const LoginPage = () => {
  const [login, { isLoading }] = useLoginMutation();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data) => {
    try {
      const response = await login(data).unwrap();
      if (response.success) {
        toast.success(response.message);
        navigate("/auth/protected");
      }
    } catch (error) {
      console.error(error.data);
      toast.error(error.data.error.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
      <div>
        <Input type="text" placeholder="Username or email" spellCheck={false} aria-invalid={!!errors.usernameOrEmail} {...register("usernameOrEmail")} />
        {errors.usernameOrEmail && <p className="mt-1 text-sm text-red-500 font-light">{errors.usernameOrEmail.message}</p>}
      </div>

      <div>
        <Input type="text" placeholder="Password" spellCheck={false} {...register("password")} aria-invalid={!!errors.password} />
        <div className="flex items-center justify-between mt-1 font-light">
          <p className="text-sm text-red-500 min-h-4">{errors.password?.message}</p>
          <button onClick={() => navigate("/auth/forgot-password")} type="button" className="text-sm text-blue-700 transition-colors hover:text-blue-700/75 cursor-pointer">
            Forgot password?
          </button>
        </div>
      </div>

      <Button type="submit" className="w-full border-black" disabled={isLoading}>
        Login
        {isLoading ? <Spinner data-icon="inline-start" /> : ""}
      </Button>
    </form>
  );
};

export default LoginPage;
