import { api } from "@/app/api";

export const accountManagementApi = api.injectEndpoints({
  endpoints: (builder) => ({
    getCurrentUser: builder.query({
      query: () => "auth/accounts/me",
      providesTags: ["User"],
    }),

    getAdmins: builder.query({
      query: () => "auth/accounts/admins",
      providesTags: ["Admin"],
    }),

    demoteAdmin: builder.mutation({
      query: (accountId) => ({
        url: `/accounts/${accountId}/demote`,
        method: "PATCH",
      }),
      invalidatesTags: ["Admin"],
    }),

    updateStatus: builder.mutation({
      query: ({ accountId, status }) => ({
        url: `auth/accounts/${accountId}/status`,
        method: "PATCH",
        body: { status },
      }),
      invalidatesTags: ["User"],
    }),
  }),
});

export const { useGetCurrentUserQuery, useGetAdminsQuery, useDemoteAdminMutation, useUpdateStatusMutation } = accountManagementApi;
