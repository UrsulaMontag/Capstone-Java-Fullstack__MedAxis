package um_backend.models;

public record IcdSearchRequest(String q, String chapterFilter, String subtreeFilter, String includePostcoordination,
                               String useBroaderSynonyms, String useFlexiSearch, String includeKeywordResult,
                               String flatResults, String highlightingEnabled, String medicalCodingMode) {
}
