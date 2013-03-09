package com.alexshabanov.springrestapi.support;

/**
 * Represents additional error description
 *
 * @author Alexander Shabanov
 */
public final class ErrorDesc {
    private int code;
    private String reason;

    public ErrorDesc() {}

    public ErrorDesc(int code, String reason) {
        this();
        setCode(code);
        setReason(reason);
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorDesc errorDesc = (ErrorDesc) o;

        return code == errorDesc.code && !(reason != null ? !reason.equals(errorDesc.reason) : errorDesc.reason != null);

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "ErrorDesc{" +
                "code=" + getCode() +
                ", reason='" + getReason() + '\'' +
                '}';
    }
}
