package com.example.picknwhip_be.domain.shop.entity.enums;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Area {

  // 서울
  HONGDAE("홍대", "서울", List.of("마포구", "서교동", "동교동", "연남동", "망원동", "합정동", "상수동")),
  GANGNAM("강남", "서울", List.of("강남구", "역삼동", "논현동", "신사동", "청담동", "삼성동", "압구정동")),
  SEONGSU("성수", "서울", List.of("성동구", "성수동", "서울숲")),
  ITAEWON("이태원", "서울", List.of("용산구", "이태원동", "한남동", "경리단길", "용산동")),
  JAMSIL("잠실", "서울", List.of("송파구", "잠실동", "석촌동", "송리단길", "방이동")),
  MANGWON("망원", "서울", List.of("마포구", "망원동")), // 홍대랑 겹치지만 별도 선택 가능 시
  YEONNAM("연남", "서울", List.of("마포구", "연남동")),
  HANNAM("한남", "서울", List.of("용산구", "한남동")),

  // 경기
  PANGYO_BUNDANG("판교-분당", "경기", List.of("성남시", "분당구", "판교동", "백현동", "삼평동", "정자동", "서현동")),
  ILSAN("일산", "경기", List.of("고양시", "일산동구", "일산서구", "장항동")),
  SUWON_HAENGGUNG("수원-행궁동", "경기", List.of("수원시", "팔달구", "행궁동", "장안동", "신풍동")),
  BUCHEON("부천", "경기", List.of("부천시", "중동", "상동", "심곡동")),
  YONGIN("용인", "경기", List.of("용인시", "수지구", "기흥구", "처인구", "보정동")),
  ANYANG("안양", "경기", List.of("안양시", "동안구", "만안구", "범계동", "평촌동")),

  // 인천
  GUWOL("구월", "인천", List.of("남동구", "구월동")),
  SONGDO("송도", "인천", List.of("연수구", "송도동")),
  BUPYEONG("부평", "인천", List.of("부평구", "부평동")),
  CHEONGNA("청라", "인천", List.of("서구", "청라동")),

  // 부산
  SEOMYEON_JEONPO("서면-전포", "부산", List.of("부산진구", "부전동", "전포동")),
  HAEUNDAE("해운대", "부산", List.of("해운대구", "우동", "중동", "좌동")),
  GWANGALLI("광안리", "부산", List.of("수영구", "광안동", "민락동", "남천동")),
  NAMPO("남포동", "부산", List.of("중구", "남포동", "광복동")),
  PUSAN_UNIV("부산대", "부산", List.of("금정구", "장전동")),

  // 대구
  DONGSEONGRO("동성로-교동", "대구", List.of("중구", "동성로", "교동", "삼덕동")),
  SAMDEOK("삼덕동", "대구", List.of("중구", "삼덕동")),
  APSAN("앞산", "대구", List.of("남구", "대명동")),
  SUSEONGMOT("수성못", "대구", List.of("수성구", "두산동", "상동")),

  // 광주
  DONGMYEONG("동명동", "광주", List.of("동구", "동명동")),
  SANGMU("상무지구", "광주", List.of("서구", "치평동")),
  YANGNIM("양림동", "광주", List.of("남구", "양림동")),
  SUWAN("수완지구", "광주", List.of("광산구", "수완동")),

  // 대전
  DUNSAN("둔산", "대전", List.of("서구", "둔산동")),
  DAEHEUNG("대흥-은행동", "대전", List.of("중구", "대흥동", "은행동")),
  BONGMYEONG("봉명동", "대전", List.of("유성구", "봉명동")),
  JUKDONG("죽동", "대전", List.of("유성구", "죽동")),

  // 충북
  CHEONGJU_SEONGAN("청주 성안길", "충북", List.of("청주시", "상당구", "성안동")),
  GAGYEONG("가경동", "충북", List.of("청주시", "흥덕구", "가경동")),
  BOKDAE("복대동", "충북", List.of("청주시", "흥덕구", "복대동", "지웰시티")),

  // 충남
  CHEONAN_BULDANG("천안 불당", "충남", List.of("천안시", "서북구", "불당동")),
  SINBU("신부동", "충남", List.of("천안시", "동남구", "신부동")),
  ASAN_BAEBANG("아산 배방", "충남", List.of("아산시", "배방읍")),

  // 전북
  JEONJU_GAEKSA("전주 객사", "전북", List.of("전주시", "완산구", "고사동", "객사")),
  SINSIGAJI("신시가지", "전북", List.of("전주시", "완산구", "효자동")),
  JEONBUK_UNIV("전북대", "전북", List.of("전주시", "덕진구", "덕진동")),

  // 전남
  YEOSU_OCEAN("여수 해양공원", "전남", List.of("여수시", "종화동", "중앙동")),
  SUNCHEON_LAKE("순천 호수공원", "전남", List.of("순천시", "왕지동", "조례동")),
  MOKPO_PEACE("목포 평화광장", "전남", List.of("목포시", "상동")),

  // 경북
  HWANGRIDANGIL("경주 황리단길", "경북", List.of("경주시", "황남동")),
  YEONGILDAE("포항 영일대", "경북", List.of("포항시", "북구", "두호동")),
  GUMI_INDONG("구미 인동", "경북", List.of("구미시", "인동")),

  // 경남
  SANGNAM("창원 상남동", "경남", List.of("창원시", "성산구", "상남동")),
  GAROSU("가로수길", "경남", List.of("창원시", "의창구", "용호동")),
  JINJU_PYEONGGEO("진주 평거동", "경남", List.of("진주시", "평거동")),
  GIMHAE_NAEOE("김해 내외동", "경남", List.of("김해시", "내동", "외동")),

  // 제주
  NOHYONG("노형-연동", "제주", List.of("제주시", "노형동", "연동")),
  CITY_HALL("시청 대학로", "제주", List.of("제주시", "이도이동")),
  AEWOL("애월", "제주", List.of("제주시", "애월읍")),
  SEONGSAN("성산", "제주", List.of("서귀포시", "성산읍"));

  private final String subRegionName;
  private final String city;
  private final List<String> searchKeywords;

  public static Area findBySubRegionName(String name) {
    return Arrays.stream(values())
        .filter(area -> area.subRegionName.equals(name))
        .findFirst()
        .orElse(null);
  }
}
