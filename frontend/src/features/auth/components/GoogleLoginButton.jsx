import { GoogleLogin } from "@react-oauth/google";
import { useGoogleLoginMutation } from "../api/authApi";
import { Spinner } from "@/components/ui/spinner";

export default function GoogleLoginButton() {
  const [googleLogin, { isLoading }] = useGoogleLoginMutation();
  return (
    <div className="relative">
      <GoogleLogin
        theme="filled_black"
        shape="pill"
        size="medium"
        onSuccess={async (credentialResponse) => {
          try {
            await googleLogin(credentialResponse.credential).unwrap();
          } catch (error) {
            console.error(error);
          }
        }}
        onError={() => {
          console.log("Login Failed");
        }}
      />
      {isLoading && (
        <div className="absolute inset-0 flex items-center justify-center rounded-md bg-white/75">
          <Spinner />
        </div>
      )}
    </div>
  );
}
