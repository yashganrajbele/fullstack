import { toast } from "sonner";
import { api } from "../../../app/api";
import { setToken } from "../authSlice";

export const authApi = api.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation({
      query: (credentials) => ({ url: "/auth/login", method: "POST", body: credentials }),
      async onQueryStarted(arg, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          const accessToken = data.data.accessToken;
          localStorage.setItem("token", accessToken);
          dispatch(setToken(accessToken));
        } catch (error) {
          console.error("Login failed:", error);
        }
      },
    }),

    registerUser: builder.mutation({
      query: (userData) => ({ url: "/auth/register", method: "POST", body: userData }),
      async onQueryStarted(arg, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          const accessToken = data.data.accessToken;
          localStorage.setItem("token", accessToken);
          dispatch(setToken(accessToken));
        } catch (error) {
          console.error("Registration failed:", error);
        }
      },
    }),

    googleLogin: builder.mutation({
      query: (idToken) => ({ url: "/auth/google", method: "POST", body: { idToken } }),
      async onQueryStarted(arg, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          const accessToken = data.data.accessToken;
          localStorage.setItem("token", accessToken);
          dispatch(setToken(accessToken));
        } catch (error) {
          console.error("Google login failed:", error);
        }
      },
    }),

    changePassword: builder.mutation({
      query: (data) => ({ url: "/auth/change-password", method: "POST", body: data }),
    }),

    sendPasswordResetOtp: builder.mutation({
      query: (data) => ({ url: "/auth/send-password-reset-otp", method: "POST", body: data }),
    }),

    resetPassword: builder.mutation({
      query: (data) => ({ url: "/auth/reset-password", method: "POST", body: data }),
    }),

    sendEmailVerificationOtp: builder.mutation({
      query: (data) => ({ url: "/auth/send-email-verification-otp", method: "POST", body: data }),
    }),

    verifyEmail: builder.mutation({
      query: (data) => ({ url: "/auth/verify-email", method: "POST", body: data }),
      invalidatesTags: ["User"],
    }),
  }),
});

export const { useLoginMutation, useRegisterUserMutation, useGoogleLoginMutation, useChangePasswordMutation, useSendPasswordResetOtpMutation, useResetPasswordMutation, useSendEmailVerificationOtpMutation, useVerifyEmailMutation } = authApi;
