package com.andreidodu.constants;

public interface JobInstanceConst {
    int STATUS_CREATED = 0;
    int STATUS_WORKER_WORK_REQUEST = 10;
    int STATUS_WORK_PROVIDER_WORK_REQUEST_ACCEPT = 20;
    int STATUS_WORKER_WORK_START = 30;
    int STATUS_WORKER_WORK_END = 40;
    int STATUS_WORK_PROVIDER_WORK_RECEIVED = 50;
    int STATUS_WORK_PROVIDER_WORK_APPROVE = 60;
    int STATUS_WORK_PROVIDER_PAYMENT = 70;
    int STATUS_WORKER_RECEIVED_PAYMENT = 80;
    int STATUS_PROCESS_COMPLETE = 90;

}
