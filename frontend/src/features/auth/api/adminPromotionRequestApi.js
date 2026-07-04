import { api } from "@/app/api";

export const adminPromotionRequestApi = api.injectEndpoints({
  endpoints: (builder) => ({
    createRequest: builder.mutation({
      query: () => ({
        url: "auth/admin-promotion-requests",
        method: "POST",
      }),
      invalidatesTags: ["PromotionRequest"],
    }),

    getRequests: builder.query({
      query: () => "auth/admin-promotion-requests/me",
      providesTags: ["PromotionRequest"],
    }),

    getPendingRequests: builder.query({
      query: () => "auth/admin-promotion-requests/pending",
      providesTags: ["PromotionRequest"],
    }),

    approveRequest: builder.mutation({
      query: (requestId) => ({
        url: `auth/admin-promotion-requests/${requestId}/approve`,
        method: "PATCH",
      }),
      invalidatesTags: ["PromotionRequest", "Admin"],
    }),

    rejectRequest: builder.mutation({
      query: (requestId) => ({
        url: `auth/admin-promotion-requests/${requestId}/reject`,
        method: "PATCH",
      }),
      invalidatesTags: ["PromotionRequest"],
    }),
  }),
});

export const { useCreateRequestMutation, useGetRequestsQuery, useGetPendingRequestsQuery, useApproveRequestMutation, useRejectRequestMutation } = adminPromotionRequestApi;
