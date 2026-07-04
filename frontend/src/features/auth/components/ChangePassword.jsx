import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useChangePasswordMutation } from "../api/authApi";
import { changePasswordSchema } from "../validation/changePasswordSchema";
import { toast } from "sonner";
import { Spinner } from "@/components/ui/spinner";

const ChangePassword = ({ setActiveView }) => {
  const [changePassword, { isLoading }] = useChangePasswordMutation();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(changePasswordSchema),
  });

  const onSubmit = async (data) => {
    try {
      const response = await changePassword(data).unwrap();
      if (response.success) {
        toast.success(response.message);
        setActiveView("HOME");
      }
    } catch (error) {
      toast.error(error.data.error.message);
    }
  };

  return (
    <>
      <h1 className="mb-2 text-lg font-extralight">Change password</h1>
      <form className="space-y-5 h-60 max-h-60" onSubmit={handleSubmit(onSubmit)}>
        <div>
          <Input type="text" placeholder="Current password" spellCheck={false} aria-invalid={!!errors.currentPassword} {...register("currentPassword")} />
          {errors.currentPassword && <p className="mt-1 text-sm text-red-500 font-light">{errors.currentPassword.message}</p>}
        </div>

        <div>
          <Input type="text" placeholder="New password" spellCheck={false} aria-invalid={!!errors.newPassword} {...register("newPassword")} />
          {errors.newPassword && <p className="mt-1 text-sm text-red-500 font-light">{errors.newPassword.message}</p>}
        </div>

        <Button type="submit" className="w-full border-black" disabled={isLoading}>
          Change
          {isLoading ? <Spinner data-icon="inline-start" /> : ""}
        </Button>
      </form>
    </>
  );
};

export default ChangePassword;
