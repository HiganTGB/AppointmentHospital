export type AppointmentResponseModel = {
    id: number;
    at: string; // datetime
    profile?: number;
    doctor?: number;
    state?: number;
    number?: number;
}
